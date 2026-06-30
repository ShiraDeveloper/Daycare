package com.example.daycare.service;

import com.example.daycare.model.DailyAttendance;
import com.example.daycare.model.Parent;
import com.example.daycare.repository.NannyRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

/**
 * Wraps all outbound email. Every send is best-effort: failures (e.g. no SMTP
 * server available) are logged and swallowed so scheduled jobs and request
 * flows never break because of mail problems.
 */
@Service
public class NotificationService {

    private static final Logger log = LoggerFactory.getLogger(NotificationService.class);

    private final JavaMailSender mailSender;
    private final NannyRepository nannyRepository;

    private final String from;
    private final String educatorFallback;
    private final String baseUrl;

    public NotificationService(JavaMailSender mailSender,
                               NannyRepository nannyRepository,
                               @Value("${app.mail.from}") String from,
                               @Value("${app.mail.educator-fallback}") String educatorFallback,
                               @Value("${app.base-url}") String baseUrl) {
        this.mailSender = mailSender;
        this.nannyRepository = nannyRepository;
        this.from = from;
        this.educatorFallback = educatorFallback;
        this.baseUrl = baseUrl;
    }

    /** Sends the morning "is your child coming today?" email with confirmation links. */
    public void sendAbsenceInquiry(DailyAttendance attendance) {
        final String parentEmail = parentEmailOf(attendance);
        if (parentEmail == null) {
            log.warn("No parent email for child id={}, skipping absence inquiry",
                    attendance.getChild().getIdChild());
            return;
        }

        final String childName = attendance.getChild().getName();
        final String arriveLink = confirmLink(attendance.getConfirmationToken(), "ARRIVING");
        final String absentLink = confirmLink(attendance.getConfirmationToken(), "ABSENT");

        final String body = """
                Hello,

                We have not yet recorded attendance for %s today.
                Please let us know:

                  Coming / arriving late: %s
                  Absent today:           %s

                Thank you,
                The Daycare Team
                """.formatted(childName, arriveLink, absentLink);

        send(parentEmail, "Attendance needed for " + childName, body);
    }

    /** Immediately alerts the educators after a parent confirms arrival/absence. */
    public void sendEducatorAlert(DailyAttendance attendance) {
        final String childName = attendance.getChild().getName();
        final String body = """
                Parent update for %s (%s):

                Status: %s
                Remarks: %s

                This was confirmed by the parent just now.
                """.formatted(
                childName,
                attendance.getLogDate(),
                attendance.getAttendanceStatus(),
                attendance.getRemarks() == null ? "-" : attendance.getRemarks());

        for (String educator : educatorEmails()) {
            send(educator, "Parent update: " + childName + " is " + attendance.getAttendanceStatus(), body);
        }
    }

    /** Sends the end-of-day summary email to the parent. */
    public void sendDailySummary(DailyAttendance attendance, String summaryBody) {
        final String parentEmail = parentEmailOf(attendance);
        if (parentEmail == null) {
            log.warn("No parent email for child id={}, skipping daily summary",
                    attendance.getChild().getIdChild());
            return;
        }
        send(parentEmail, "Daily summary for " + attendance.getChild().getName(), summaryBody);
    }

    private void send(String to, String subject, String body) {
        try {
            final SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
            log.info("Sent email '{}' to {}", subject, to);
        } catch (Exception ex) {
            log.warn("Failed to send email '{}' to {}: {}", subject, to, ex.getMessage());
        }
    }

    private String confirmLink(String token, String decision) {
        return UriComponentsBuilder.fromUriString(baseUrl)
                .path("/api/public/confirm")
                .queryParam("token", token)
                .queryParam("decision", decision)
                .toUriString();
    }

    private String parentEmailOf(DailyAttendance attendance) {
        final Parent parent = attendance.getChild().getParent();
        if (parent == null || parent.getEmail() == null || parent.getEmail().isBlank()) {
            return null;
        }
        return parent.getEmail();
    }

    private List<String> educatorEmails() {
        final List<String> emails = nannyRepository.findAll().stream()
                .map(n -> n.getEmail())
                .filter(e -> e != null && !e.isBlank())
                .toList();
        return emails.isEmpty() ? List.of(educatorFallback) : emails;
    }
}

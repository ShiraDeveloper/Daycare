package com.example.daycare.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

/**
 * Triggers the daily notification jobs. The heavy lifting (and transaction
 * boundary) lives in {@link AttendanceWorkflowService}; this class only owns
 * the schedule.
 */
@Component
public class DailyWorkflowScheduler {

    private static final Logger log = LoggerFactory.getLogger(DailyWorkflowScheduler.class);

    private final AttendanceWorkflowService workflowService;

    public DailyWorkflowScheduler(AttendanceWorkflowService workflowService) {
        this.workflowService = workflowService;
    }

    /** 09:00 daily: ask parents about children with no attendance recorded. */
    @Scheduled(cron = "${app.schedule.morning-attendance}", zone = "${app.schedule.zone}")
    public void morningAttendanceCheck() {
        final LocalDate today = LocalDate.now();
        log.info("Running morning attendance inquiries for {}", today);
        final int sent = workflowService.runMorningAttendanceInquiries(today);
        log.info("Morning attendance inquiries sent: {}", sent);
    }

    /** 17:00 daily: send end-of-day summaries to parents. */
    @Scheduled(cron = "${app.schedule.evening-summary}", zone = "${app.schedule.zone}")
    public void eveningSummary() {
        final LocalDate today = LocalDate.now();
        log.info("Running end-of-day summaries for {}", today);
        final int sent = workflowService.runEndOfDaySummaries(today);
        log.info("End-of-day summaries sent: {}", sent);
    }
}

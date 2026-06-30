package com.example.daycare.service;

import com.example.daycare.dto.ActivitiesRequest;
import com.example.daycare.dto.DailyActivityDto;
import com.example.daycare.dto.DailyAttendanceDto;
import com.example.daycare.dto.GuestParticipationDto;
import com.example.daycare.dto.GuestParticipationRequest;
import com.example.daycare.dto.MealSelectionRequest;
import com.example.daycare.dto.PresenceRequest;
import com.example.daycare.model.AttendanceStatus;
import com.example.daycare.model.Child;
import com.example.daycare.model.DailyActivity;
import com.example.daycare.model.DailyAttendance;
import com.example.daycare.model.GuestParticipation;
import com.example.daycare.model.GuestPerformer;
import com.example.daycare.model.MealTemplate;
import com.example.daycare.model.WorkflowState;
import com.example.daycare.repository.ChildRepository;
import com.example.daycare.repository.DailyAttendanceRepository;
import com.example.daycare.repository.MealTemplateRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class AttendanceWorkflowService {

    private final DailyAttendanceRepository attendanceRepository;
    private final ChildRepository childRepository;
    private final MealTemplateRepository mealTemplateRepository;
    private final GuestPerformerService guestPerformerService;
    private final NotificationService notificationService;

    public AttendanceWorkflowService(DailyAttendanceRepository attendanceRepository,
                                     ChildRepository childRepository,
                                     MealTemplateRepository mealTemplateRepository,
                                     GuestPerformerService guestPerformerService,
                                     NotificationService notificationService) {
        this.attendanceRepository = attendanceRepository;
        this.childRepository = childRepository;
        this.mealTemplateRepository = mealTemplateRepository;
        this.guestPerformerService = guestPerformerService;
        this.notificationService = notificationService;
    }

    // ---------------------------------------------------------------------
    // Educator-facing progressive workflow
    // ---------------------------------------------------------------------

    /** Step 1: educator marks the child present, opening the rest of the workflow. */
    @Transactional
    public DailyAttendanceDto markPresence(Integer childId, PresenceRequest request) {
        final DailyAttendance attendance = getOrCreateToday(childId);
        requireState(attendance, WorkflowState.ATTENDANCE_PENDING);

        attendance.setAttendanceStatus(AttendanceStatus.PRESENT);
        attendance.setWorkflowState(WorkflowState.PRESENT);
        appendRemarks(attendance, request.getRemarks());
        attendance.getChild().setDailyStatus(AttendanceStatus.PRESENT);

        return toDto(attendanceRepository.save(attendance));
    }

    /** Step 2: breakfast selection (prompted from MealTemplate). */
    @Transactional
    public DailyAttendanceDto recordBreakfast(Integer childId, MealSelectionRequest request) {
        final DailyAttendance attendance = getExistingToday(childId);
        requireState(attendance, WorkflowState.PRESENT);

        attendance.setBreakfast(loadMeal(request.getMealTemplateId()));
        attendance.setWorkflowState(WorkflowState.BREAKFAST_RECORDED);
        appendRemarks(attendance, request.getRemarks());

        return toDto(attendanceRepository.save(attendance));
    }

    /** Step 3: lunch selection. */
    @Transactional
    public DailyAttendanceDto recordLunch(Integer childId, MealSelectionRequest request) {
        final DailyAttendance attendance = getExistingToday(childId);
        requireState(attendance, WorkflowState.BREAKFAST_RECORDED);

        attendance.setLunch(loadMeal(request.getMealTemplateId()));
        attendance.setWorkflowState(WorkflowState.LUNCH_RECORDED);
        appendRemarks(attendance, request.getRemarks());

        return toDto(attendanceRepository.save(attendance));
    }

    /** Step 4: daily activities. Auto-completes the day unless a guest is scheduled. */
    @Transactional
    public DailyAttendanceDto recordActivities(Integer childId, ActivitiesRequest request) {
        final DailyAttendance attendance = getExistingToday(childId);
        requireState(attendance, WorkflowState.LUNCH_RECORDED);

        attendance.getActivities().clear();
        for (DailyActivityDto item : request.getActivities()) {
            final DailyActivity activity = new DailyActivity();
            activity.setDailyAttendance(attendance);
            activity.setActivityType(item.getActivityType());
            activity.setDescription(item.getDescription());
            activity.setRemarks(item.getRemarks());
            attendance.getActivities().add(activity);
        }
        appendRemarks(attendance, request.getRemarks());

        if (isGuestScheduled(attendance.getLogDate())) {
            attendance.setWorkflowState(WorkflowState.ACTIVITIES_RECORDED);
        } else {
            attendance.setWorkflowState(WorkflowState.COMPLETED);
        }

        return toDto(attendanceRepository.save(attendance));
    }

    /** Step 5 (conditional): mandatory guest-performer participation per child. */
    @Transactional
    public DailyAttendanceDto recordGuestParticipation(Integer childId, GuestParticipationRequest request) {
        final DailyAttendance attendance = getExistingToday(childId);
        requireState(attendance, WorkflowState.ACTIVITIES_RECORDED);

        final GuestPerformer guest = guestPerformerService.findScheduledFor(attendance.getLogDate())
                .orElseThrow(() -> new IllegalStateException(
                        "No guest performer is scheduled for " + attendance.getLogDate()));

        attendance.getGuestParticipations().clear();
        final GuestParticipation participation = new GuestParticipation();
        participation.setDailyAttendance(attendance);
        participation.setGuestPerformer(guest);
        participation.setParticipated(Boolean.TRUE.equals(request.getParticipated()));
        participation.setRemarks(request.getRemarks());
        attendance.getGuestParticipations().add(participation);

        attendance.setWorkflowState(WorkflowState.COMPLETED);
        appendRemarks(attendance, request.getRemarks());

        return toDto(attendanceRepository.save(attendance));
    }

    @Transactional(readOnly = true)
    public List<DailyAttendanceDto> getTodayBoard() {
        final LocalDate today = LocalDate.now();
        final boolean guestScheduled = isGuestScheduled(today);
        final List<DailyAttendanceDto> board = new ArrayList<>();

        for (Child child : childRepository.findAll()) {
            final DailyAttendance attendance = attendanceRepository
                    .findByChild_IdChildAndLogDate(child.getIdChild(), today)
                    .orElse(null);
            if (attendance != null) {
                board.add(toDto(attendance));
            } else {
                board.add(pendingDto(child, today, guestScheduled));
            }
        }
        return board;
    }

    // ---------------------------------------------------------------------
    // Parent-facing confirmation
    // ---------------------------------------------------------------------

    /** Parent clicks the emailed link; records their decision and alerts educators. */
    @Transactional
    public DailyAttendanceDto confirmByParent(String token, String decision, String remarks) {
        final DailyAttendance attendance = attendanceRepository.findByConfirmationToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid or expired confirmation link"));

        final AttendanceStatus status = switch (decision == null ? "" : decision.trim().toUpperCase()) {
            case "ARRIVING", "ARRIVE", "PRESENT", "LATE" -> AttendanceStatus.ARRIVING_LATE;
            case "ABSENT", "NO" -> AttendanceStatus.ABSENT;
            default -> throw new IllegalArgumentException("decision must be ARRIVING or ABSENT");
        };

        attendance.setAttendanceStatus(status);
        attendance.setParentConfirmed(true);
        attendance.setParentConfirmedAt(LocalDateTime.now());
        appendRemarks(attendance, remarks);
        attendance.getChild().setDailyStatus(status);

        final DailyAttendance saved = attendanceRepository.save(attendance);
        notificationService.sendEducatorAlert(saved);
        return toDto(saved);
    }

    // ---------------------------------------------------------------------
    // Scheduler support
    // ---------------------------------------------------------------------

    /**
     * Morning job (runs inside a transaction so lazy parent data resolves):
     * for every child without a record for {@code date}, creates an
     * ATTENDANCE_PENDING record with a confirmation token and emails the parent.
     * Returns the number of inquiries sent.
     */
    @Transactional
    public int runMorningAttendanceInquiries(LocalDate date) {
        int sent = 0;
        for (Child child : childRepository.findAll()) {
            if (attendanceRepository.existsByChild_IdChildAndLogDate(child.getIdChild(), date)) {
                continue;
            }
            final DailyAttendance attendance = new DailyAttendance();
            attendance.setChild(child);
            attendance.setLogDate(date);
            attendance.setAttendanceStatus(AttendanceStatus.UNKNOWN);
            attendance.setWorkflowState(WorkflowState.ATTENDANCE_PENDING);
            attendance.setConfirmationToken(UUID.randomUUID().toString());
            final DailyAttendance saved = attendanceRepository.save(attendance);
            notificationService.sendAbsenceInquiry(saved);
            sent++;
        }
        return sent;
    }

    /**
     * Evening job (runs inside a transaction so lazy activity/guest/parent data
     * resolves): aggregates and emails the end-of-day summary for each child
     * that has a record for {@code date}. Returns the number of summaries sent.
     */
    @Transactional
    public int runEndOfDaySummaries(LocalDate date) {
        int sent = 0;
        for (DailyAttendance attendance : attendanceRepository.findByLogDate(date)) {
            notificationService.sendDailySummary(attendance, buildSummary(attendance));
            sent++;
        }
        return sent;
    }

    /** Builds the human-readable end-of-day summary body for one child. */
    public String buildSummary(DailyAttendance a) {
        final StringBuilder sb = new StringBuilder();
        sb.append("Daily summary for ").append(a.getChild().getName())
                .append(" (").append(a.getLogDate()).append(")\n\n");
        sb.append("Attendance: ").append(a.getAttendanceStatus()).append('\n');
        sb.append("Breakfast: ").append(mealLabel(a.getBreakfast())).append('\n');
        sb.append("Lunch: ").append(mealLabel(a.getLunch())).append('\n');

        sb.append("Activities: ");
        if (a.getActivities().isEmpty()) {
            sb.append("none recorded");
        } else {
            sb.append('\n');
            for (DailyActivity act : a.getActivities()) {
                sb.append("  - ").append(act.getActivityType());
                if (act.getDescription() != null) {
                    sb.append(": ").append(act.getDescription());
                }
                sb.append('\n');
            }
        }
        sb.append('\n');

        if (!a.getGuestParticipations().isEmpty()) {
            sb.append("Guest performances:\n");
            for (GuestParticipation gp : a.getGuestParticipations()) {
                sb.append("  - ").append(gp.getGuestPerformer().getName())
                        .append(gp.isParticipated() ? ": participated" : ": did not participate")
                        .append('\n');
            }
            sb.append('\n');
        }

        if (a.getRemarks() != null && !a.getRemarks().isBlank()) {
            sb.append("Remarks: ").append(a.getRemarks()).append('\n');
        }

        sb.append("\nThank you,\nThe Daycare Team\n");
        return sb.toString();
    }

    // ---------------------------------------------------------------------
    // Internals
    // ---------------------------------------------------------------------

    private DailyAttendance getOrCreateToday(Integer childId) {
        final LocalDate today = LocalDate.now();
        return attendanceRepository.findByChild_IdChildAndLogDate(childId, today)
                .orElseGet(() -> {
                    final Child child = loadChild(childId);
                    final DailyAttendance attendance = new DailyAttendance();
                    attendance.setChild(child);
                    attendance.setLogDate(today);
                    attendance.setAttendanceStatus(AttendanceStatus.UNKNOWN);
                    attendance.setWorkflowState(WorkflowState.ATTENDANCE_PENDING);
                    attendance.setConfirmationToken(UUID.randomUUID().toString());
                    return attendanceRepository.save(attendance);
                });
    }

    private DailyAttendance getExistingToday(Integer childId) {
        return attendanceRepository.findByChild_IdChildAndLogDate(childId, LocalDate.now())
                .orElseThrow(() -> new IllegalStateException(
                        "No attendance started for child id=" + childId + " today; mark presence first"));
    }

    private Child loadChild(Integer childId) {
        return childRepository.findById(childId)
                .orElseThrow(() -> new IllegalArgumentException("No child found with id: " + childId));
    }

    private MealTemplate loadMeal(Integer mealTemplateId) {
        return mealTemplateRepository.findById(mealTemplateId)
                .orElseThrow(() -> new IllegalArgumentException("No meal template found with id: " + mealTemplateId));
    }

    private boolean isGuestScheduled(LocalDate date) {
        return guestPerformerService.findScheduledFor(date).isPresent();
    }

    private void requireState(DailyAttendance attendance, WorkflowState expected) {
        if (attendance.getWorkflowState() != expected) {
            throw new IllegalStateException(
                    "Invalid workflow transition: expected state " + expected
                            + " but child is in state " + attendance.getWorkflowState());
        }
    }

    private void appendRemarks(DailyAttendance attendance, String remarks) {
        if (remarks == null || remarks.isBlank()) {
            return;
        }
        final String existing = attendance.getRemarks();
        attendance.setRemarks(existing == null || existing.isBlank() ? remarks : existing + " | " + remarks);
    }

    private String mealLabel(MealTemplate meal) {
        return meal == null ? "not recorded" : meal.getName();
    }

    // ---------------------------------------------------------------------
    // DTO mapping
    // ---------------------------------------------------------------------

    private DailyAttendanceDto toDto(DailyAttendance a) {
        final DailyAttendanceDto dto = new DailyAttendanceDto();
        dto.setId(a.getId());
        dto.setChildId(a.getChild().getIdChild());
        dto.setChildName(a.getChild().getName());
        dto.setLogDate(a.getLogDate());
        dto.setAttendanceStatus(a.getAttendanceStatus());
        dto.setWorkflowState(a.getWorkflowState());
        final boolean guestScheduled = isGuestScheduled(a.getLogDate());
        dto.setGuestScheduled(guestScheduled);
        dto.setNextStep(nextStep(a.getWorkflowState(), guestScheduled));

        if (a.getBreakfast() != null) {
            dto.setBreakfastId(a.getBreakfast().getId());
            dto.setBreakfastName(a.getBreakfast().getName());
        }
        if (a.getLunch() != null) {
            dto.setLunchId(a.getLunch().getId());
            dto.setLunchName(a.getLunch().getName());
        }

        dto.setParentConfirmed(a.isParentConfirmed());
        dto.setParentConfirmedAt(a.getParentConfirmedAt());
        dto.setRemarks(a.getRemarks());

        dto.setActivities(a.getActivities().stream().map(this::toActivityDto).toList());
        dto.setGuestParticipations(a.getGuestParticipations().stream().map(this::toParticipationDto).toList());
        return dto;
    }

    private DailyAttendanceDto pendingDto(Child child, LocalDate date, boolean guestScheduled) {
        final DailyAttendanceDto dto = new DailyAttendanceDto();
        dto.setChildId(child.getIdChild());
        dto.setChildName(child.getName());
        dto.setLogDate(date);
        dto.setAttendanceStatus(AttendanceStatus.UNKNOWN);
        dto.setWorkflowState(WorkflowState.ATTENDANCE_PENDING);
        dto.setGuestScheduled(guestScheduled);
        dto.setNextStep(nextStep(WorkflowState.ATTENDANCE_PENDING, guestScheduled));
        dto.setActivities(List.of());
        dto.setGuestParticipations(List.of());
        return dto;
    }

    private DailyActivityDto toActivityDto(DailyActivity activity) {
        final DailyActivityDto dto = new DailyActivityDto();
        dto.setId(activity.getId());
        dto.setActivityType(activity.getActivityType());
        dto.setDescription(activity.getDescription());
        dto.setRemarks(activity.getRemarks());
        return dto;
    }

    private GuestParticipationDto toParticipationDto(GuestParticipation gp) {
        final GuestParticipationDto dto = new GuestParticipationDto();
        dto.setId(gp.getId());
        dto.setGuestPerformerId(gp.getGuestPerformer().getId());
        dto.setGuestPerformerName(gp.getGuestPerformer().getName());
        dto.setParticipated(gp.isParticipated());
        dto.setRemarks(gp.getRemarks());
        return dto;
    }

    private String nextStep(WorkflowState state, boolean guestScheduled) {
        return switch (state) {
            case ATTENDANCE_PENDING -> "MARK_PRESENCE";
            case PRESENT -> "RECORD_BREAKFAST";
            case BREAKFAST_RECORDED -> "RECORD_LUNCH";
            case LUNCH_RECORDED -> "RECORD_ACTIVITIES";
            case ACTIVITIES_RECORDED -> guestScheduled ? "RECORD_GUEST_PARTICIPATION" : "COMPLETE";
            case COMPLETED -> "DONE";
        };
    }
}

package com.example.daycare.dto;

import com.example.daycare.model.AttendanceStatus;
import com.example.daycare.model.WorkflowState;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class DailyAttendanceDto {
    private Integer id;
    private Integer childId;
    private String childName;
    private LocalDate logDate;

    private AttendanceStatus attendanceStatus;
    private WorkflowState workflowState;

    /** Computed hint for the educator UI: the next action to perform. */
    private String nextStep;

    /** Whether a guest performer is scheduled for this date. */
    private boolean guestScheduled;

    private Integer breakfastId;
    private String breakfastName;
    private Integer lunchId;
    private String lunchName;

    private boolean parentConfirmed;
    private LocalDateTime parentConfirmedAt;

    private String remarks;

    private List<DailyActivityDto> activities;
    private List<GuestParticipationDto> guestParticipations;
}

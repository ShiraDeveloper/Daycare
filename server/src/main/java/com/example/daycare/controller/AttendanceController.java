package com.example.daycare.controller;

import com.example.daycare.dto.ActivitiesRequest;
import com.example.daycare.dto.DailyAttendanceDto;
import com.example.daycare.dto.GuestParticipationRequest;
import com.example.daycare.dto.MealSelectionRequest;
import com.example.daycare.dto.PresenceRequest;
import com.example.daycare.service.AttendanceWorkflowService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    private final AttendanceWorkflowService workflowService;

    public AttendanceController(AttendanceWorkflowService workflowService) {
        this.workflowService = workflowService;
    }

    /** Educator board for today: every child with current state and next step. */
    @GetMapping("/today")
    public List<DailyAttendanceDto> today() {
        return workflowService.getTodayBoard();
    }

    @PostMapping("/{childId}/present")
    public DailyAttendanceDto markPresent(@PathVariable Integer childId,
                                          @RequestBody(required = false) PresenceRequest request) {
        return workflowService.markPresence(childId, request == null ? new PresenceRequest() : request);
    }

    @PostMapping("/{childId}/breakfast")
    public DailyAttendanceDto recordBreakfast(@PathVariable Integer childId,
                                              @Valid @RequestBody MealSelectionRequest request) {
        return workflowService.recordBreakfast(childId, request);
    }

    @PostMapping("/{childId}/lunch")
    public DailyAttendanceDto recordLunch(@PathVariable Integer childId,
                                          @Valid @RequestBody MealSelectionRequest request) {
        return workflowService.recordLunch(childId, request);
    }

    @PostMapping("/{childId}/activities")
    public DailyAttendanceDto recordActivities(@PathVariable Integer childId,
                                               @Valid @RequestBody ActivitiesRequest request) {
        return workflowService.recordActivities(childId, request);
    }

    @PostMapping("/{childId}/guest-participation")
    public DailyAttendanceDto recordGuestParticipation(@PathVariable Integer childId,
                                                       @Valid @RequestBody GuestParticipationRequest request) {
        return workflowService.recordGuestParticipation(childId, request);
    }
}

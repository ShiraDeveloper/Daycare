package com.example.daycare.controller;

import com.example.daycare.dto.DailyAttendanceDto;
import com.example.daycare.service.AttendanceWorkflowService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Public (unauthenticated) endpoints reached from links in parent emails.
 */
@RestController
@RequestMapping("/api/public")
public class PublicConfirmationController {

    private final AttendanceWorkflowService workflowService;

    public PublicConfirmationController(AttendanceWorkflowService workflowService) {
        this.workflowService = workflowService;
    }

    /** Parent clicks the link from the morning email to confirm arrival/absence. */
    @GetMapping(value = "/confirm", produces = MediaType.TEXT_PLAIN_VALUE)
    public String confirm(@RequestParam String token,
                          @RequestParam String decision,
                          @RequestParam(required = false) String remarks) {
        final DailyAttendanceDto result = workflowService.confirmByParent(token, decision, remarks);
        return "Thank you. We recorded that " + result.getChildName()
                + " is " + result.getAttendanceStatus() + " for " + result.getLogDate() + ".";
    }
}

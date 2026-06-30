package com.example.daycare.model;

/**
 * Progressive state of an educator's daily workflow for a single child.
 * Transitions are strictly ordered and enforced in the service layer:
 * ATTENDANCE_PENDING -> PRESENT -> BREAKFAST_RECORDED -> LUNCH_RECORDED
 * -> ACTIVITIES_RECORDED -> COMPLETED.
 * When a guest performer is scheduled, COMPLETED is only reachable after
 * guest participation has been recorded.
 */
public enum WorkflowState {
    ATTENDANCE_PENDING,
    PRESENT,
    BREAKFAST_RECORDED,
    LUNCH_RECORDED,
    ACTIVITIES_RECORDED,
    COMPLETED
}

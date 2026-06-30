package com.example.daycare.model;

public enum AttendanceStatus {
    /** No information yet for the day. */
    UNKNOWN,
    /** Child is present at the daycare. */
    PRESENT,
    /** Parent reported the child will be absent. */
    ABSENT,
    /** Parent reported the child will arrive later than usual. */
    ARRIVING_LATE
}

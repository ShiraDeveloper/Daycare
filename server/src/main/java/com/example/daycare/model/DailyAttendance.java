package com.example.daycare.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * The per-child, per-day aggregate that drives the progressive educator
 * workflow and stores everything needed for the end-of-day summary.
 */
@Getter
@Setter
@Entity
@Table(
        name = "daily_attendance",
        uniqueConstraints = @UniqueConstraint(columnNames = {"child_id", "log_date"})
)
public class DailyAttendance {

    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "child_id")
    private Child child;

    @Column(name = "log_date", nullable = false)
    private LocalDate logDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttendanceStatus attendanceStatus = AttendanceStatus.UNKNOWN;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private WorkflowState workflowState = WorkflowState.ATTENDANCE_PENDING;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "breakfast_template_id")
    private MealTemplate breakfast;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lunch_template_id")
    private MealTemplate lunch;

    /** Opaque token embedded in the parent confirmation link. */
    @Column(unique = true)
    private String confirmationToken;

    private boolean parentConfirmed;

    private LocalDateTime parentConfirmedAt;

    private String remarks;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "dailyAttendance", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DailyActivity> activities = new ArrayList<>();

    @OneToMany(mappedBy = "dailyAttendance", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<GuestParticipation> guestParticipations = new ArrayList<>();

    @PrePersist
    void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
}

package com.example.daycare.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Entity
public class Child {

    @Id
    @GeneratedValue
    private Integer idChild;

    private String name;
    private String imageChild;
    private boolean sensitivity;

    @Enumerated(EnumType.STRING)
    private ChildLevel level;

    private LocalDate birthDate;
    private boolean arrived;

    /** Quick-access current-day status; the per-day source of truth is DailyAttendance. */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttendanceStatus dailyStatus = AttendanceStatus.UNKNOWN;

    @OneToMany(mappedBy = "child")
    private List<Dairy> dairyList;

    @OneToMany(mappedBy = "child")
    private List<DailyAttendance> dailyAttendances;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_Parent")
    private Parent parent;
}

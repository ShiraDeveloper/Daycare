package com.example.daycare.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@Entity
public class Dairy {

    @Id
    @GeneratedValue
    private Integer idDairy;

    // "date" and "hour" are SQL reserved words, so map to safe column names.
    @Column(name = "dairy_date")
    private LocalDate date;

    @Column(name = "dairy_hour")
    private LocalTime hour;

    @Enumerated(EnumType.STRING)
    private Activity doing;

    private String remark;
    private String images;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "child_id")
    private Child child;
}

package com.example.daycare.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@Entity
public class GuestPerformer {

    @Id
    @GeneratedValue
    private Integer id;

    @Column(nullable = false)
    private String name;

    /** The date this performer visits; drives the mandatory participation prompt. */
    @Column(name = "performance_date", nullable = false)
    private LocalDate performanceDate;

    private String description;

    private String remarks;
}

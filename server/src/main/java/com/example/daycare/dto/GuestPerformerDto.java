package com.example.daycare.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class GuestPerformerDto {
    private Integer id;
    private String name;
    private LocalDate performanceDate;
    private String description;
    private String remarks;
}

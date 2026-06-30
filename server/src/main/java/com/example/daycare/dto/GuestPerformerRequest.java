package com.example.daycare.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class GuestPerformerRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Performance date is required")
    private LocalDate performanceDate;

    private String description;

    private String remarks;
}

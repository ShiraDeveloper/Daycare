package com.example.daycare.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ActivitiesRequest {

    @NotEmpty(message = "At least one activity is required")
    @Valid
    private List<DailyActivityDto> activities;

    private String remarks;
}

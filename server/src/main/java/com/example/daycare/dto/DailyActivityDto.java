package com.example.daycare.dto;

import com.example.daycare.model.ActivityType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/** Used both as a response item and as a request item when recording activities. */
@Getter
@Setter
public class DailyActivityDto {

    private Integer id;

    @NotNull(message = "Activity type is required")
    private ActivityType activityType;

    private String description;

    private String remarks;
}

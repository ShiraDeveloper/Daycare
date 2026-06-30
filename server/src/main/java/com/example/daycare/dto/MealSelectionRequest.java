package com.example.daycare.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MealSelectionRequest {

    @NotNull(message = "mealTemplateId is required")
    private Integer mealTemplateId;

    private String remarks;
}

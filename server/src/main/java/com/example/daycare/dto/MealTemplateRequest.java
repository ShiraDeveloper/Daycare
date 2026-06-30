package com.example.daycare.dto;

import com.example.daycare.model.MealType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MealTemplateRequest {

    @NotBlank(message = "Name is required")
    private String name;

    @NotNull(message = "Meal type is required")
    private MealType mealType;

    private String description;
}

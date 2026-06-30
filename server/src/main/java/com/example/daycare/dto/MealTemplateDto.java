package com.example.daycare.dto;

import com.example.daycare.model.MealType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MealTemplateDto {
    private Integer id;
    private String name;
    private MealType mealType;
    private String description;
    private boolean active;
}

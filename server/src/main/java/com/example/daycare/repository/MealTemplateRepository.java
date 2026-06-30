package com.example.daycare.repository;

import com.example.daycare.model.MealTemplate;
import com.example.daycare.model.MealType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MealTemplateRepository extends JpaRepository<MealTemplate, Integer> {
    List<MealTemplate> findByActiveTrue();

    List<MealTemplate> findByMealTypeAndActiveTrue(MealType mealType);
}

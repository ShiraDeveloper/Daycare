package com.example.daycare.controller;

import com.example.daycare.dto.MealTemplateDto;
import com.example.daycare.dto.MealTemplateRequest;
import com.example.daycare.model.MealType;
import com.example.daycare.service.MealTemplateService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/meal-templates")
public class MealTemplateController {

    private final MealTemplateService mealTemplateService;

    public MealTemplateController(MealTemplateService mealTemplateService) {
        this.mealTemplateService = mealTemplateService;
    }

    @PostMapping
    public ResponseEntity<MealTemplateDto> create(@Valid @RequestBody MealTemplateRequest request) {
        return new ResponseEntity<>(mealTemplateService.create(request), HttpStatus.CREATED);
    }

    @GetMapping
    public List<MealTemplateDto> list(@RequestParam(required = false) MealType type) {
        return type == null ? mealTemplateService.listAll() : mealTemplateService.listByType(type);
    }
}

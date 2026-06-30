package com.example.daycare.service;

import com.example.daycare.dto.MealTemplateDto;
import com.example.daycare.dto.MealTemplateRequest;
import com.example.daycare.model.MealTemplate;
import com.example.daycare.model.MealType;
import com.example.daycare.repository.MealTemplateRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MealTemplateService {

    private final MealTemplateRepository mealTemplateRepository;

    public MealTemplateService(MealTemplateRepository mealTemplateRepository) {
        this.mealTemplateRepository = mealTemplateRepository;
    }

    @Transactional
    public MealTemplateDto create(MealTemplateRequest request) {
        final MealTemplate template = new MealTemplate();
        template.setName(request.getName());
        template.setMealType(request.getMealType());
        template.setDescription(request.getDescription());
        template.setActive(true);
        return toDto(mealTemplateRepository.save(template));
    }

    @Transactional(readOnly = true)
    public List<MealTemplateDto> listAll() {
        return mealTemplateRepository.findByActiveTrue().stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public List<MealTemplateDto> listByType(MealType mealType) {
        return mealTemplateRepository.findByMealTypeAndActiveTrue(mealType).stream()
                .map(this::toDto)
                .toList();
    }

    MealTemplateDto toDto(MealTemplate template) {
        final MealTemplateDto dto = new MealTemplateDto();
        dto.setId(template.getId());
        dto.setName(template.getName());
        dto.setMealType(template.getMealType());
        dto.setDescription(template.getDescription());
        dto.setActive(template.isActive());
        return dto;
    }
}

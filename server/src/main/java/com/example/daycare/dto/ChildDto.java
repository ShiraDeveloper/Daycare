package com.example.daycare.dto;

import com.example.daycare.model.ChildLevel;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ChildDto {
    private Integer idChild;
    private String name;
    private String imageChild;
    private boolean sensitivity;
    private ChildLevel level;
    private LocalDate birthDate;
    private boolean arrived;
}

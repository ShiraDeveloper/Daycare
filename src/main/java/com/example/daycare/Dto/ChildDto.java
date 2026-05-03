package com.example.daycare.Dto;

import com.example.daycare.model.eLevel;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Date;
@Getter
@Setter
public class ChildDto {
    private int idChild;
    private String name;
  private String imageChild;
  //  private byte[] imageChild;
    private boolean sensitivity;
    @Enumerated(EnumType.STRING)
    private eLevel level;
    private LocalDate birthDate;
    private boolean Arrived;
}

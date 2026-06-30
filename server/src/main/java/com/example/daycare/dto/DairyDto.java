package com.example.daycare.dto;

import com.example.daycare.model.Activity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class DairyDto {
    private Integer idDairy;
    private LocalDate date;
    private LocalTime hour;
    private Activity doing;
    private String remark;
    private String images;
    private Integer childId;
}

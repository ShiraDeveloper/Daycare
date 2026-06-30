package com.example.daycare.dto;

import com.example.daycare.model.UserRole;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NannyDto {
    private Integer idNanny;
    private String name;
    private String phone;
    private String email;
    private UserRole role;
}

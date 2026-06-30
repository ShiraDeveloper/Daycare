package com.example.daycare.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GuestParticipationDto {
    private Integer id;
    private Integer guestPerformerId;
    private String guestPerformerName;
    private boolean participated;
    private String remarks;
}

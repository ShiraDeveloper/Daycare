package com.example.daycare.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GuestParticipationRequest {

    @NotNull(message = "participated is required")
    private Boolean participated;

    private String remarks;
}

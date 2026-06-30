package com.example.daycare.controller;

import com.example.daycare.dto.GuestPerformerDto;
import com.example.daycare.dto.GuestPerformerRequest;
import com.example.daycare.service.GuestPerformerService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/guest-performers")
public class GuestPerformerController {

    private final GuestPerformerService guestPerformerService;

    public GuestPerformerController(GuestPerformerService guestPerformerService) {
        this.guestPerformerService = guestPerformerService;
    }

    @PostMapping
    public ResponseEntity<GuestPerformerDto> create(@Valid @RequestBody GuestPerformerRequest request) {
        return new ResponseEntity<>(guestPerformerService.create(request), HttpStatus.CREATED);
    }

    @GetMapping
    public List<GuestPerformerDto> list() {
        return guestPerformerService.listAll();
    }
}

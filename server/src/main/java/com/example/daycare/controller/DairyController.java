package com.example.daycare.controller;

import com.example.daycare.dto.DairyDto;
import com.example.daycare.service.DairyService;
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
@RequestMapping("/api/Dairy")
public class DairyController {

    private final DairyService dairyService;

    public DairyController(DairyService dairyService) {
        this.dairyService = dairyService;
    }

    @GetMapping("/getAll")
    public List<DairyDto> getAll() {
        return dairyService.getAll();
    }

    @PostMapping("/addDairy")
    public ResponseEntity<DairyDto> addDairy(@Valid @RequestBody DairyDto dairyDto) {
        return new ResponseEntity<>(dairyService.add(dairyDto), HttpStatus.CREATED);
    }
}

package com.example.daycare.controller;

import com.example.daycare.Dto.ParentDto;
import com.example.daycare.Service.ParentService;
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
@RequestMapping("/api/Parent")
public class ParentController {

    private final ParentService parentService;

    public ParentController(ParentService parentService) {
        this.parentService = parentService;
    }

    @GetMapping("/getAll")
    public List<ParentDto> getAll() {
        return parentService.getAll();
    }

    @PostMapping("/addParent")
    public ResponseEntity<ParentDto> addParent(@Valid @RequestBody ParentDto parentDto) {
        return new ResponseEntity<>(parentService.add(parentDto), HttpStatus.CREATED);
    }
}

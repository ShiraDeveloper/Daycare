package com.example.daycare.controller;

import com.example.daycare.dto.ChildDto;
import com.example.daycare.service.ChildService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/Child")
public class ChildController {

    private final ChildService childService;

    public ChildController(ChildService childService) {
        this.childService = childService;
    }

    @GetMapping("/getAll")
    public List<ChildDto> getAll() {
        return childService.getAll();
    }

    @PostMapping("/addChild")
    public ResponseEntity<ChildDto> addChild(@Valid @RequestBody ChildDto childDto) {
        return new ResponseEntity<>(childService.add(childDto), HttpStatus.CREATED);
    }

    @PostMapping("/addChildDto")
    public ResponseEntity<ChildDto> addChildWithImage(@RequestPart("file") MultipartFile file,
                                                      @Valid @RequestPart("child") ChildDto childDto) {
        return new ResponseEntity<>(childService.addWithImage(file, childDto), HttpStatus.CREATED);
    }
}

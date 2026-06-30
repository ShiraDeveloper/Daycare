package com.example.daycare.controller;

import com.example.daycare.dto.NannyDto;
import com.example.daycare.service.NannyService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/Nanny")
public class NannyController {

    private final NannyService nannyService;

    public NannyController(NannyService nannyService) {
        this.nannyService = nannyService;
    }

    @GetMapping("/getAll")
    public List<NannyDto> getAll() {
        return nannyService.getAll();
    }
}

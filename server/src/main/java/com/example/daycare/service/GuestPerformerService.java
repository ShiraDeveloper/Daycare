package com.example.daycare.service;

import com.example.daycare.dto.GuestPerformerDto;
import com.example.daycare.dto.GuestPerformerRequest;
import com.example.daycare.model.GuestPerformer;
import com.example.daycare.repository.GuestPerformerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class GuestPerformerService {

    private final GuestPerformerRepository guestPerformerRepository;

    public GuestPerformerService(GuestPerformerRepository guestPerformerRepository) {
        this.guestPerformerRepository = guestPerformerRepository;
    }

    @Transactional
    public GuestPerformerDto create(GuestPerformerRequest request) {
        final GuestPerformer performer = new GuestPerformer();
        performer.setName(request.getName());
        performer.setPerformanceDate(request.getPerformanceDate());
        performer.setDescription(request.getDescription());
        performer.setRemarks(request.getRemarks());
        return toDto(guestPerformerRepository.save(performer));
    }

    @Transactional(readOnly = true)
    public List<GuestPerformerDto> listAll() {
        return guestPerformerRepository.findAll().stream().map(this::toDto).toList();
    }

    @Transactional(readOnly = true)
    public Optional<GuestPerformer> findScheduledFor(LocalDate date) {
        return guestPerformerRepository.findFirstByPerformanceDate(date);
    }

    GuestPerformerDto toDto(GuestPerformer performer) {
        final GuestPerformerDto dto = new GuestPerformerDto();
        dto.setId(performer.getId());
        dto.setName(performer.getName());
        dto.setPerformanceDate(performer.getPerformanceDate());
        dto.setDescription(performer.getDescription());
        dto.setRemarks(performer.getRemarks());
        return dto;
    }
}

package com.example.daycare.service;

import com.example.daycare.dto.DairyDto;
import com.example.daycare.repository.ChildRepository;
import com.example.daycare.repository.DairyRepository;
import com.example.daycare.model.Child;
import com.example.daycare.model.Dairy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DairyService {

    private final DairyRepository dairyRepository;
    private final ChildRepository childRepository;
    private final MapStructMapper mapper;

    public DairyService(DairyRepository dairyRepository,
                        ChildRepository childRepository,
                        MapStructMapper mapper) {
        this.dairyRepository = dairyRepository;
        this.childRepository = childRepository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<DairyDto> getAll() {
        return mapper.toListDairyDto(dairyRepository.findAll());
    }

    @Transactional
    public DairyDto add(DairyDto dairyDto) {
        final Dairy dairy = mapper.toDairy(dairyDto);

        if (dairyDto.getChildId() != null) {
            final Child child = childRepository.findById(dairyDto.getChildId())
                    .orElseThrow(() -> new IllegalArgumentException(
                            "No child found with id: " + dairyDto.getChildId()));
            dairy.setChild(child);
        }

        return mapper.toDairyDto(dairyRepository.save(dairy));
    }
}

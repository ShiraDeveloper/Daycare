package com.example.daycare.Service;

import com.example.daycare.Dto.DairyDto;
import com.example.daycare.Repository.DairyRepository;
import com.example.daycare.model.Dairy1;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class DairyService {

    private final DairyRepository dairyRepository;
    private final MapStructMapper mapper;

    public DairyService(DairyRepository dairyRepository, MapStructMapper mapper) {
        this.dairyRepository = dairyRepository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<DairyDto> getAll() {
        return mapper.toListDairyDto(dairyRepository.findAll());
    }

    @Transactional
    public DairyDto add(DairyDto dairyDto) {
        final Dairy1 dairy = mapper.toDairy(dairyDto);
        return mapper.toDairyDto(dairyRepository.save(dairy));
    }
}

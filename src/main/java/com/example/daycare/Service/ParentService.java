package com.example.daycare.Service;

import com.example.daycare.Dto.ParentDto;
import com.example.daycare.Repository.ParentRepository;
import com.example.daycare.model.Parent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ParentService {

    private final ParentRepository parentRepository;
    private final MapStructMapper mapper;

    public ParentService(ParentRepository parentRepository, MapStructMapper mapper) {
        this.parentRepository = parentRepository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public List<ParentDto> getAll() {
        return mapper.toListParentDto(parentRepository.findAll());
    }

    @Transactional
    public ParentDto add(ParentDto parentDto) {
        final Parent parent = mapper.toParent(parentDto);
        return mapper.toParentDto(parentRepository.save(parent));
    }
}

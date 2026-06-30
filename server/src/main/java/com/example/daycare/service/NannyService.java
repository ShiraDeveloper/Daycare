package com.example.daycare.service;

import com.example.daycare.dto.NannyDto;
import com.example.daycare.dto.RegisterRequest;
import com.example.daycare.repository.NannyRepository;
import com.example.daycare.exception.EmailAlreadyExistsException;
import com.example.daycare.model.Nanny;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class NannyService {

    private final NannyRepository nannyRepository;
    private final PasswordEncoder passwordEncoder;
    private final MapStructMapper mapper;

    public NannyService(NannyRepository nannyRepository,
                        PasswordEncoder passwordEncoder,
                        MapStructMapper mapper) {
        this.nannyRepository = nannyRepository;
        this.passwordEncoder = passwordEncoder;
        this.mapper = mapper;
    }

    @Transactional
    public NannyDto register(RegisterRequest request) {
        final String email = request.getEmail().trim().toLowerCase();
        nannyRepository.findByEmail(email).ifPresent(existing -> {
            throw new EmailAlreadyExistsException("A nanny with email '" + email + "' already exists");
        });

        final Nanny nanny = new Nanny();
        nanny.setName(request.getName());
        nanny.setPhone(request.getPhone());
        nanny.setEmail(email);
        nanny.setPassword(passwordEncoder.encode(request.getPassword()));

        return mapper.toNannyDto(nannyRepository.save(nanny));
    }

    @Transactional(readOnly = true)
    public List<NannyDto> getAll() {
        return mapper.toListNannyDto(nannyRepository.findAll());
    }
}

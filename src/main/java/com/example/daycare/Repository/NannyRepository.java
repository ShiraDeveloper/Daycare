package com.example.daycare.Repository;

import com.example.daycare.model.Nanny;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NannyRepository extends JpaRepository<Nanny, Integer> {
    Optional<Nanny> findByEmail(String email);
}

package com.example.daycare.repository;

import com.example.daycare.model.GuestPerformer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface GuestPerformerRepository extends JpaRepository<GuestPerformer, Integer> {
    List<GuestPerformer> findByPerformanceDate(LocalDate performanceDate);

    Optional<GuestPerformer> findFirstByPerformanceDate(LocalDate performanceDate);
}

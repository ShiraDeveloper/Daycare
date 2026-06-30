package com.example.daycare.repository;

import com.example.daycare.model.Dairy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DairyRepository extends JpaRepository<Dairy, Integer> {
}

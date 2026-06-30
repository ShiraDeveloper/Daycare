package com.example.daycare.repository;

import com.example.daycare.model.GuestParticipation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuestParticipationRepository extends JpaRepository<GuestParticipation, Integer> {
}

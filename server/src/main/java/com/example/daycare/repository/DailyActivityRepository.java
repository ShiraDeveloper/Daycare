package com.example.daycare.repository;

import com.example.daycare.model.DailyActivity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DailyActivityRepository extends JpaRepository<DailyActivity, Integer> {
}

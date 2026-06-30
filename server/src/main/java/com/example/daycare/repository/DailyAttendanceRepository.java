package com.example.daycare.repository;

import com.example.daycare.model.DailyAttendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DailyAttendanceRepository extends JpaRepository<DailyAttendance, Integer> {

    List<DailyAttendance> findByLogDate(LocalDate logDate);

    Optional<DailyAttendance> findByChild_IdChildAndLogDate(Integer childId, LocalDate logDate);

    Optional<DailyAttendance> findByConfirmationToken(String confirmationToken);

    boolean existsByChild_IdChildAndLogDate(Integer childId, LocalDate logDate);
}

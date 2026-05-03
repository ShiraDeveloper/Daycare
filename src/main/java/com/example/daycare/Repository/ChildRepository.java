package com.example.daycare.Repository;

import com.example.daycare.model.Child;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChildRepository extends JpaRepository<Child,Integer> {
}

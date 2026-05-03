package com.example.daycare.Repository;

import com.example.daycare.model.Nanny;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;

public interface NannyRepository extends JpaRepository<Nanny,Integer> {
    UserDetails findByEmail(String email);
}

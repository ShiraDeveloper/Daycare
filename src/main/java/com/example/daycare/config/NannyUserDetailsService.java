package com.example.daycare.config;

import com.example.daycare.Repository.NannyRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class NannyUserDetailsService implements UserDetailsService {

    private final NannyRepository nannyRepository;

    public NannyUserDetailsService(NannyRepository nannyRepository) {
        this.nannyRepository = nannyRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return nannyRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("No nanny found with email: " + email));
    }
}

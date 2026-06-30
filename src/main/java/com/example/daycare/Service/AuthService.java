package com.example.daycare.Service;

import com.example.daycare.Dto.AuthenticateRequest;
import com.example.daycare.Dto.NannyDto;
import com.example.daycare.Dto.RegisterRequest;
import com.example.daycare.config.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final NannyService nannyService;

    public AuthService(AuthenticationManager authenticationManager,
                       UserDetailsService userDetailsService,
                       JwtUtil jwtUtil,
                       NannyService nannyService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.nannyService = nannyService;
    }

    @Transactional
    public NannyDto register(RegisterRequest request) {
        return nannyService.register(request);
    }

    @Transactional(readOnly = true)
    public String authenticate(AuthenticateRequest request) {
        final String email = request.getEmail().trim().toLowerCase();
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, request.getPass()));
        final UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        return jwtUtil.generateToken(userDetails);
    }
}

package com.example.daycare.controller;
import com.example.daycare.Dto.AuthenticateRequest;
import com.example.daycare.Repository.NannyRepository;
import com.example.daycare.config.JwtUtil;
import com.example.daycare.model.Nanny;
import jdk.jfr.Frequency;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authorization.method.AuthorizeReturnObject;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")

public class AuthController {
    @Autowired
    private final JwtUtil jwtUtil;
    @Autowired
    private  final NannyRepository nannyRepository;
    public AuthController(//AuthenticationManager authenticationManager,
                          NannyRepository nannyRepository,
                          JwtUtil jwt) {
        //this.authenticationManager = authenticationManager;
        this.nannyRepository=nannyRepository;
        this.jwtUtil=jwt;
    }

    @PostMapping("/authenticate")
    public ResponseEntity<String> authenticate(@RequestBody AuthenticateRequest authenticateRequest)
    {
        // authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(auth.getEmail(),auth.getPass()));
        final  UserDetails userDetails=nannyRepository.findByEmail(authenticateRequest.getEmail());
        //final UserDetails userDetails=userDao.findUserByEmail(auth.getEmail());
        if(userDetails!=null)
        {
            String token=jwtUtil.generateToken(userDetails);
            return  ResponseEntity.ok(token);
        }
        return  ResponseEntity.status(400).body("user was not found...");
    }

}

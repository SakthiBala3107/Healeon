package com.pm.authservice.service;


import com.pm.authservice.dto.LoginRequestDTO;
import com.pm.authservice.model.User;
import com.pm.authservice.utills.JwtUtill;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    //    dependency injection
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtill jwtUtill;

    public String authenticate(LoginRequestDTO loginRequestDTO) {

//
        String token = userService.findByEmail(loginRequestDTO.getEmail())
                .filter(u -> passwordEncoder.matches(loginRequestDTO.getPassword(), u.getPassword()))
                .map(u -> jwtUtill.generateToken(u.getEmail(), u.getRole()))
//                .map(this::generateTokenForUser)
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        return token;
    }


    //    VALIDATE TOKEN
    public boolean validateToken(String token) {
        try {
            jwtUtill.validateToken(token);
            return true;
        } catch (JwtException e) {
            return false;

        }

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}

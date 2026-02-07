package com.pm.authservice.dto;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

@Service

public class LoginResponseDTO {

    private final String token;


    public LoginResponseDTO(String token) {
        this.token = token;
    }


    public String getToken() {
        return token;
    }
}



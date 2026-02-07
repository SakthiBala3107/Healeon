package com.pm.authservice.controller;

import com.pm.authservice.dto.LoginRequestDTO;
import com.pm.authservice.dto.LoginResponseDTO;
import com.pm.authservice.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
//import com.pm.authservice.service.AuthService;


import java.util.Optional;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    //    DI
    private final AuthService authService;

    @Operation(summary = "Generate token on user Login")
    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginRequestDTO loginRequestDto) {

        return Optional.ofNullable(authService.authenticate(loginRequestDto))  // wrap possible null in Optional
                .filter(token -> !token.isEmpty())                              // remove empty strings
                .map(token -> ResponseEntity.ok(new LoginResponseDTO(token)))  // map to 200 OK response
                .orElseGet(() -> ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()); // 401 if null/empty
    }


}

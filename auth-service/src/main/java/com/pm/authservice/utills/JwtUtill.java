package com.pm.authservice.utills;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.SignatureException;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtill {

    private final Key secretKey;

    //    DECODE THE TOKEN FROM THE DB FOR VALIDATION
    public JwtUtill(@Value("${jwt.secret}") String secret) {
        byte[] keyBytes = Base64.getDecoder().decode(secret); // decode Base64
        this.secretKey = Keys.hmacShaKeyFor(keyBytes);       // use decoded bytes
    }

    public Key getSecretKey() {
        return secretKey;
    }


    //    GENERATE TOKEN
    public String generateToken(String email, String role) {

        return Jwts.builder()
                .subject(email)
                .claim("role", role)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
                .signWith(secretKey)
                .compact();
    }

    //    VALIDATE TOKEN
    public void validateToken(String token) {
        if (token == null || token.isBlank()) {
            throw new JwtException("JWT token is missing or empty");
        }

        try {
            Jwts.parser()
                    .verifyWith((SecretKey) secretKey)
                    .build()
                    .parseSignedClaims(token); //gurad protect my signature from the scoundrels
        } catch (io.jsonwebtoken.security.SignatureException e) {
            throw new JwtException("Invalid JWT signature", e);
        } catch (JwtException e) {
            throw new JwtException("JWT validation failed: " + e.getMessage(), e);
        }
    }
//
}


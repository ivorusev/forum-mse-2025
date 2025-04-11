package com.edu.pwc.forum.security;


import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import javax.crypto.SecretKey;

@Slf4j
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private Long jwtExpirationMs;

    private SecretKey key;

    @PostConstruct
    public void init() {
        // TODO: Initialize the secret key
    }

    public String generateToken(Authentication authentication) {
        // TODO: Implement token generation logic
        return null;
    }

    public boolean validateJwtToken(String token) {
        // TODO: Implement token validation logic
        return false;
    }

    public String getUsername(String token) {
        // TODO: Implement logic to extract username from token
        return null;
    }

    public UsernamePasswordAuthenticationToken getAuthentication(String token, UserDetailsService userDetailsService) {
        // TODO: Implement logic to create authentication object
        return null;
    }
}

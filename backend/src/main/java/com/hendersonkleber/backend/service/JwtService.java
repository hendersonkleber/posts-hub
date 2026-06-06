package com.hendersonkleber.backend.service;

import com.hendersonkleber.backend.config.JwtConfig;
import com.hendersonkleber.backend.entity.User;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class JwtService {
    private final JwtConfig jwtConfig;
    private final JwtEncoder jwtEncoder;

    public JwtService(JwtConfig jwtConfig, JwtEncoder jwtEncoder) {
        this.jwtConfig = jwtConfig;
        this.jwtEncoder = jwtEncoder;
    }

    public Jwt generateAccessToken(User user) {
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .id(UUID.randomUUID().toString())
                .subject(user.getId().toString())
                .issuer(this.jwtConfig.getIssuer())
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(this.jwtConfig.getTokenExpiresIn()))
                .build();

        return this.jwtEncoder.encode(JwtEncoderParameters.from(claims));
    }
}

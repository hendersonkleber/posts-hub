package com.hendersonkleber.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;

@Configuration
public class JwtConfig {
    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.issuer}")
    private String issuer;

    @Value("${jwt.token.expires-in}")
    private Long tokenExpiresIn;

    @Value("${jwt.refresh-token.expires-in}")
    private Long refreshTokenExpiresIn;

    public SecretKey getSecretKey() {
        return new SecretKeySpec(this.secretKey.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
    }

    public String getIssuer() {
        return issuer;
    }

    public Long getTokenExpiresIn() {
        return tokenExpiresIn;
    }

    public Long getRefreshTokenExpiresIn() {
        return refreshTokenExpiresIn;
    }
}

package com.hendersonkleber.backend.service;

import com.hendersonkleber.backend.config.JwtConfig;
import com.hendersonkleber.backend.dto.AuthResponse;
import com.hendersonkleber.backend.dto.LoginRequest;
import com.hendersonkleber.backend.dto.RefreshRequest;
import com.hendersonkleber.backend.dto.RegisterRequest;
import com.hendersonkleber.backend.entity.User;
import com.hendersonkleber.backend.entity.UserSession;
import com.hendersonkleber.backend.exception.AuthException;
import com.hendersonkleber.backend.repository.UserRepository;
import com.hendersonkleber.backend.repository.UserSessionRepository;
import com.hendersonkleber.backend.util.OpaqueTokenUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final UserSessionRepository userSessionRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtConfig jwtConfig;
    private final JwtEncoder jwtEncoder;

    public AuthService(UserRepository userRepository, UserSessionRepository userSessionRepository, PasswordEncoder passwordEncoder, JwtConfig jwtConfig, JwtEncoder jwtEncoder) {
        this.userRepository = userRepository;
        this.userSessionRepository = userSessionRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtConfig = jwtConfig;
        this.jwtEncoder = jwtEncoder;
    }

    public AuthResponse register(RegisterRequest request) {
        var userAlreadyExists = this.userRepository.existsByEmail(request.email());

        if (userAlreadyExists) {
            throw new AuthException("User with this email already registered");
        }

        var user = new User();
        user.setName(request.name());
        user.setEmail(request.email());
        user.setPassword(this.passwordEncoder.encode(request.password()));
        this.userRepository.save(user);

        Jwt accessToken = this.generateAccessToken(user);
        String refreshToken = this.generateRefreshToken(user);

        return new AuthResponse(
                accessToken.getTokenValue(),
                refreshToken
        );
    }

    @Transactional
    public AuthResponse login(LoginRequest request) {
        // buscar usuario
        var user = this.userRepository
                .findByEmail(request.email())
                .orElseThrow(() -> new AuthException("invalid credentials"));

        // validar senha
        var isValidPassword = this.passwordEncoder.matches(request.password(), user.getPassword());

        if (!isValidPassword) {
            throw new AuthException("invalid credentials");
        }

        // revogar todos os refreshtokens
        this.userSessionRepository.revokeByUserId(user.getId(), Instant.now());

        // gerar accesstoken
        Jwt accessToken = this.generateAccessToken(user);

        // gerar refreshtoken
        String refreshToken = this.generateRefreshToken(user);

        return new AuthResponse(
                accessToken.getTokenValue(),
                refreshToken
        );
    }

    @Transactional
    public AuthResponse refresh(RefreshRequest request) {
        // buscar session
        String refreshTokenHash = OpaqueTokenUtil.generateHash(request.refreshToken());
        UserSession session = this.userSessionRepository.findByRefreshTokenHash(refreshTokenHash).orElseThrow(() -> new AuthException("Session not found"));

        // validar se o session está expirado
        if (Instant.now().isAfter(session.getExpiresAt())) {
            throw new AuthException("Session has expired");
        }

        // validar se o session está revogado
        if (session.getRevokedAt() != null) {
            throw new AuthException("Session already revoked");
        }

        // revogar session atual
        this.revokeRefreshToken(refreshTokenHash);

        // obter usuario
        User user = this.userRepository.getReferenceById(session.getUser().getId());

        // gerar novo access token
        Jwt accessToken = this.generateAccessToken(user);

        // gerar novo refresh token
        String refreshToken = this.generateRefreshToken(user);

        // retornar valores
        return new AuthResponse(accessToken.getTokenValue(), refreshToken);
    }

    @Transactional
    public void logout(UUID userId, String refreshToken) {
        // gerar refreshTokenHash
        String refreshTokenHash = OpaqueTokenUtil.generateHash(refreshToken);

        // verificar se session existe
        boolean sessionExists = this.userSessionRepository.existsByRefreshTokenHashAndUserId(refreshTokenHash, userId);

        // caso não existir retornar exception
        if (!sessionExists) {
            throw new AuthException("Refresh token invalid");
        }

        // revogar session
        this.revokeRefreshToken(refreshTokenHash);
    }

    private Jwt generateAccessToken(User user) {
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .id(UUID.randomUUID().toString())
                .subject(user.getId().toString())
                .issuer(this.jwtConfig.getIssuer())
                .issuedAt(Instant.now())
                .expiresAt(Instant.now().plusSeconds(this.jwtConfig.getTokenExpiresIn()))
                .build();

        return this.jwtEncoder.encode(JwtEncoderParameters.from(claims));
    }

    private String generateRefreshToken(User user) {
        String opaqueToken = OpaqueTokenUtil.generate();
        String opaqueTokenHash = OpaqueTokenUtil.generateHash(opaqueToken);

        var session = new UserSession();
        session.setUser(user);
        session.setRefreshTokenHash(opaqueTokenHash);
        session.setCreatedAt(Instant.now());
        session.setExpiresAt(Instant.now().plusSeconds(this.jwtConfig.getRefreshTokenExpiresIn()));

        this.userSessionRepository.save(session);

        return opaqueToken;
    }

    private void revokeRefreshToken(String refreshTokenHash) {
        int rows = this.userSessionRepository.revoke(refreshTokenHash, Instant.now());

        if (rows == 0) {
            throw new AuthException("Session already revoked");
        }
    }
}

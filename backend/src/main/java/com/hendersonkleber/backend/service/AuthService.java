package com.hendersonkleber.backend.service;

import com.hendersonkleber.backend.dto.AuthResponse;
import com.hendersonkleber.backend.dto.LoginRequest;
import com.hendersonkleber.backend.dto.RegisterRequest;
import com.hendersonkleber.backend.entity.User;
import com.hendersonkleber.backend.exception.AuthException;
import com.hendersonkleber.backend.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
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

        Jwt accessToken = this.jwtService.generateAccessToken(user);

        return new AuthResponse(
                accessToken.getTokenValue()
        );
    }

    public AuthResponse login(LoginRequest request) {
        var user = this.userRepository
                .findByEmail(request.email())
                .orElseThrow(() -> new AuthException("invalid credentials"));

        var isValidPassword = this.passwordEncoder.matches(request.password(), user.getPassword());

        if (!isValidPassword) {
            throw new AuthException("invalid credentials");
        }

        Jwt accessToken = this.jwtService.generateAccessToken(user);

        return new AuthResponse(
                accessToken.getTokenValue()
        );
    }
}

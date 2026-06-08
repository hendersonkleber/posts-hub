package com.hendersonkleber.backend.controller;

import com.hendersonkleber.backend.dto.AuthResponse;
import com.hendersonkleber.backend.dto.LoginRequest;
import com.hendersonkleber.backend.dto.RefreshRequest;
import com.hendersonkleber.backend.dto.RegisterRequest;
import com.hendersonkleber.backend.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping(path = "/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping(path = "/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest body) {
        var response = this.authService.register(body);
        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest body) {
        var response = this.authService.login(body);
        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/refresh")
    public ResponseEntity<AuthResponse> refresh(@RequestBody RefreshRequest body) {
        var response = this.authService.refresh(body);
        return ResponseEntity.ok(response);
    }

    @PostMapping(path = "/logout")
    public ResponseEntity<Void> logout(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody RefreshRequest body
    ) {
        var userId = UUID.fromString(jwt.getSubject());
        var refreshToken = body.refreshToken();
        this.authService.logout(userId, refreshToken);
        return ResponseEntity.noContent().build();
    }
}

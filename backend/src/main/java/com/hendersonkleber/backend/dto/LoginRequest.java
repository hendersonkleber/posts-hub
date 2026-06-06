package com.hendersonkleber.backend.dto;

public record LoginRequest(
        String email,
        String password
) {
}

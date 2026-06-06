package com.hendersonkleber.backend.dto;

import com.hendersonkleber.backend.entity.User;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String name,
        String email
) {
    public static UserResponse from(User entity) {
        return new UserResponse(entity.getId(), entity.getName(), entity.getEmail());
    }
}

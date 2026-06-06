package com.hendersonkleber.backend.service;

import com.hendersonkleber.backend.dto.UserResponse;
import com.hendersonkleber.backend.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public UserResponse findById(UUID id) {
        var user = this.userRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("User not found"));
        return UserResponse.from(user);
    }
}

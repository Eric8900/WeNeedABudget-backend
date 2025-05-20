package com.example.wnabudgetbackend.config;

import com.example.wnabudgetbackend.repository.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SecurityUtil {

    private final UserRepository userRepository;

    public SecurityUtil(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean isAuthorized(UUID userId) {
        return userRepository.findById(userId)
                .map(user -> user.getEmail().equals(getCurrentUserEmail()))
                .orElse(false);
    }

    public String getCurrentUserEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
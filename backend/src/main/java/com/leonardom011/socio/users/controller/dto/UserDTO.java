package com.leonardom011.socio.users.controller.dto;

import com.leonardom011.socio.users.entity.Role;
import com.leonardom011.socio.users.entity.User;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.NonNull;

import java.time.LocalDateTime;
import java.util.UUID;

@JsonSerialize
public record UserDTO(
        @NonNull UUID id,
        @NonNull String firstName,
        @NonNull String lastName,
        @NonNull String email,
        @NonNull String username,
        @NonNull Role role,
        @NonNull LocalDateTime createdAt,
        @NonNull LocalDateTime lastLogin,
        @NonNull LocalDateTime lastChange

) {
    public UserDTO(User user) {
        this(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getUsername(),
                user.getRole(),
                user.getCreatedAt(),
                user.getLastLogin(),
                user.getLastChange()
        );
    }
}

package com.leonardom011.socio.auth.controller.dto;

import com.leonardom011.socio.users.entity.Role;
import com.leonardom011.socio.users.entity.User;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@JsonSerialize
public record RegisterRequest(
        @NonNull String firstName,
        @NonNull String lastName,
        @NonNull String email,
        @NonNull RoleRequest role,
        @NonNull String username,
        @NonNull String password
) {

    public RegisterRequest(User user) {
        this(
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                RoleRequest.valueOf(user.getRole().toString()),
                user.getUsername(),
                user.getPassword()
        );
    }

    public User toUser(PasswordEncoder passwordEncoder, LocalDateTime localDateTime) {
        return User.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .username(username)
                .password(passwordEncoder.encode(password))
                .role(Role.valueOf(role.toString()))
                .createdAt(localDateTime)
                .lastLogin(localDateTime)
                .lastChange(localDateTime)
                .build();
    }
}

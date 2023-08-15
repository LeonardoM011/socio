package com.leonardom011.socio.auth.controller.dto;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.NonNull;

@JsonSerialize
public record AuthenticationRequest(
        @NonNull String username,
        @NonNull String password
) {}

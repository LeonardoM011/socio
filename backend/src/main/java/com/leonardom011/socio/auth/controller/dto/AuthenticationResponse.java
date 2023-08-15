package com.leonardom011.socio.auth.controller.dto;

import com.leonardom011.socio.users.controller.dto.UserDTO;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.NonNull;

@JsonSerialize
public record AuthenticationResponse(
        @NonNull String token,
        @NonNull UserDTO userData
) {}

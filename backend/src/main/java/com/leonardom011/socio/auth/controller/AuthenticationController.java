package com.leonardom011.socio.auth.controller;

import com.leonardom011.socio.auth.controller.dto.AuthenticationRequest;
import com.leonardom011.socio.auth.controller.dto.AuthenticationResponse;
import com.leonardom011.socio.auth.controller.dto.RegisterRequest;
import com.leonardom011.socio.auth.service.AuthenticationService;
import com.leonardom011.socio.exception.CouldNotRegisterException;
import com.leonardom011.socio.exception.WrongUsernameOrPasswordException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth/")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("register")
    @Operation(
            summary = "Register new user",
            description = "Register Service",
            responses = {
                    @ApiResponse(responseCode = "200", ref = "register"),
                    @ApiResponse(responseCode = "403", ref = "registerFailed") })
    public ResponseEntity<?> register(
            @Validated
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "form for registration",
                    content = @Content(schema = @Schema(implementation = RegisterRequest.class)))
            RegisterRequest request) throws CouldNotRegisterException {

        AuthenticationResponse response = authenticationService.register(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("authenticate")
    @Operation(
            summary = "Authenticate existing user",
            description = "Authentication Service",
            responses = {
                    @ApiResponse(responseCode = "200", ref = "authenticate"),
                    @ApiResponse(responseCode = "403", ref = "authenticateFailed") })
    public ResponseEntity<?> authenticate(
            @Validated
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    required = true,
                    description = "Credentials",
                    content = @Content(schema = @Schema(implementation = AuthenticationRequest.class)))
            AuthenticationRequest request) throws WrongUsernameOrPasswordException {

        AuthenticationResponse response = authenticationService.authenticate(request);
        return ResponseEntity.ok(response);
    }

}

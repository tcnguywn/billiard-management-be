package com.backend.billiards_management.controllers;

import com.backend.billiards_management.dtos.request.auth.LoginRequest;
import com.backend.billiards_management.dtos.response.ApiResponse;
import com.backend.billiards_management.dtos.response.auth.AuthResponse;
import com.backend.billiards_management.services.keycloak.KeycloakUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AuthController {

    private final KeycloakUserService keycloakUserService;

    @PostMapping("/auth/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@RequestBody LoginRequest request) {

        AuthResponse authResponse = keycloakUserService.login(request);

        return ResponseEntity.ok(
                ApiResponse.<AuthResponse>builder()
                        .status(HttpStatus.OK.value())
                        .message("Login success")
                        .body(authResponse)
                        .build()
        );
    }
}

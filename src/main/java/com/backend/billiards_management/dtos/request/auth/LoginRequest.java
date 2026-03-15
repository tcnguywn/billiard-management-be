package com.backend.billiards_management.dtos.request.auth;

import lombok.Data;

@Data
public class LoginRequest {
    private String username;
    private String password;
}

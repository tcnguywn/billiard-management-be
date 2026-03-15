package com.backend.billiards_management.dtos.response.auth;

import com.backend.billiards_management.dtos.response.employee.EmployeeRes;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private Long expiresIn;
    private EmployeeRes employee;
}
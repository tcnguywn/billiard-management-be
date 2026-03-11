package com.backend.billiards_management.dtos.response;

import com.backend.billiards_management.entities.employee.enums.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmployeeRes {

    private Integer id;

    private String keycloakId;

    private String fullName;

    private String email;

    private String phoneNumber;

    private Role role;

    private String imageUrl;

    private Boolean isActive;
}
package com.backend.billiards_management.dtos.response.employee;

import com.backend.billiards_management.entities.employee.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRes {

    private Integer id;

    private String keycloakId;

    private String firstName;

    private String lastName;

    private String email;

    private String phoneNumber;

    private Role role;

    private String imageUrl;

    private Boolean isActive;
}
package com.backend.billiards_management.dtos.request.employee;

import com.backend.billiards_management.entities.employee.enums.Role;
import lombok.Data;

@Data
public class UpdateEmployeeReq {

    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;

    private Role role;
    private Boolean isActive;
}
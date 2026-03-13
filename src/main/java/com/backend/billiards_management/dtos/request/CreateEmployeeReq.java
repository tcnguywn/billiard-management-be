package com.backend.billiards_management.dtos.request;

import com.backend.billiards_management.entities.employee.enums.Role;
import lombok.Data;

@Data
public class CreateEmployeeReq {

    private String username;
    private String password;

    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;

    private Role role;
}
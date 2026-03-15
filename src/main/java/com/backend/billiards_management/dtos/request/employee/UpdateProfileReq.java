package com.backend.billiards_management.dtos.request.employee;

import lombok.Data;

@Data
public class UpdateProfileReq {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
}
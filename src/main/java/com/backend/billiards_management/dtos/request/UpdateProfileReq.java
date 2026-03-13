package com.backend.billiards_management.dtos.request;

import lombok.Data;

@Data
public class UpdateProfileReq {
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
}
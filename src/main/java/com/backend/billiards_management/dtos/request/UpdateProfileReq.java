package com.backend.billiards_management.dtos.request;

import lombok.Data;

@Data
public class UpdateProfileReq {
    private String fullName;
    private String email;
    private String phoneNumber;
}
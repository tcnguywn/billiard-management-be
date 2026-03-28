package com.backend.billiards_management.dtos.request.employee;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UpdateProfileReq {
//    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private MultipartFile image;
}
package com.backend.billiards_management.dtos.response.image;

import lombok.Data;

@Data
public class UploadRes {
    private int id;
    private String url;
    private String publicId;
}

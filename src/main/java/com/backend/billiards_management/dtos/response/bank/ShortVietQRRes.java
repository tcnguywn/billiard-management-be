package com.backend.billiards_management.dtos.response.bank;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ShortVietQRRes {
    private String qrCode;
    private String qrImageUrl;
}

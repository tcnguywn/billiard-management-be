package com.backend.billiards_management.dtos.response.bank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DataObjectResponse {
    private String qrCode;
    private String qrDataURL;
}

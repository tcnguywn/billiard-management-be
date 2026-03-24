package com.backend.billiards_management.dtos.response.bank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VietQRResponse {
    private String code;
    private String desc;
    private DataObjectResponse data;
}

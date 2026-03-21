package com.backend.billiards_management.dtos.request.bank;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class VietQRRequest {
    private String accountNo;
    private String accountName;
    private int acqId;
    private BigDecimal amount;
    private String addInfo;
    private String format = "text";
    private String template = "compact";
}

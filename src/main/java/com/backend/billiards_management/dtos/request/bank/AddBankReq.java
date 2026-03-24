package com.backend.billiards_management.dtos.request.bank;

import lombok.Data;

@Data
public class AddBankReq {
    private String bankName;
    private String bankCode;
    private String bankAccount;
}

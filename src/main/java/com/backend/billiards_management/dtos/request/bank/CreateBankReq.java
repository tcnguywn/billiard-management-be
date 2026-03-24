package com.backend.billiards_management.dtos.request.bank;

import lombok.Data;

@Data
public class CreateBankReq {
    private String bankBin;
    private String bankAccountNo;
    private String bankAccountName;
    private boolean bankStatus;
    private String bankName;
    private String bankShortName;
    private String bankLogo;
}

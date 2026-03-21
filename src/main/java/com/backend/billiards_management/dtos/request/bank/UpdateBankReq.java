package com.backend.billiards_management.dtos.request.bank;

import lombok.Data;

@Data
public class UpdateBankReq {
    private int id;
    private String bankBin;
    private String bankAccountNo;
    private String bankAccountName;
    private boolean bankStatus;
    private int employeeId;
}

package com.backend.billiards_management.dtos.response.bank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BankRes {
    private int id;
    private String bankBin;
    private String bankAccountNo;
    private String bankAccountName;
    private boolean bankStatus;
    private String bankName;
    private String bankShortName;
    private String bankLogo;
    private Date createdAt;
    private Date updatedAt;
}

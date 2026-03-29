package com.backend.billiards_management.dtos.response.invoice;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceRes {
    private int id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    private String paymentMethod;
    private BigDecimal serviceAmount;
    private BigDecimal productAmount;
    private BigDecimal taxAmount;
    private BigDecimal totalAmount;

    // Voucher info
    private int voucherId;
    private String voucherCode;

    // Employee info
    private int employeeId;
    private String employeeName;

    // BilliardTable info
    private int billiardTableId;
    private String billiardTableName;

    private Date createdAt;
    private Date updatedAt;

    // QR Code info (for CREDIT_CARD payment)
    private String qrCode;
    private String qrImageUrl;
}

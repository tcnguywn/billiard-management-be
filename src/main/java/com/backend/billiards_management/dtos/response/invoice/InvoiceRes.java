package com.backend.billiards_management.dtos.response.invoice;

import com.backend.billiards_management.dtos.response.order_detail.OrderDetailRes;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

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

    // Danh sách chi tiết hóa đơn
    private List<OrderDetailRes> orderDetails;

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
    private List<OrderDetailRes> orderDetailResList;
}

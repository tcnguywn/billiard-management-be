package com.backend.billiards_management.dtos.request.invoice;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class UpdateInvoiceReq {
    private int id;
    private Date startTime;
    private Date endTime;
    private String status;
    private String paymentMethod;
    private BigDecimal serviceAmount;
    private BigDecimal productAmount;
    private BigDecimal taxAmount;
    private BigDecimal totalAmount;
    private int voucherId;
    private int employeeId;
    private int billiardTableId;
}

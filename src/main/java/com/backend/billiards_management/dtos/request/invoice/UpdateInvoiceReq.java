package com.backend.billiards_management.dtos.request.invoice;

import lombok.Data;
import org.springframework.cglib.core.Local;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Data
public class UpdateInvoiceReq {
    private int id;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    private String paymentMethod;
    private int voucherId;
    private int employeeId;
    private int billiardTableId;
}

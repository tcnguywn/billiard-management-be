package com.backend.billiards_management.dtos.request.invoice;

import lombok.Data;

import java.time.LocalDateTime;


@Data
public class CreateInvoiceReq {
    private LocalDateTime startTime;
    private int employeeId;
    private int billiardTableId;
}

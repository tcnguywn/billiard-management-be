package com.backend.billiards_management.dtos.request.order_detail;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AddOrderDetailReq {
    int invoiceId;
    int quantity;
    int productId;
    LocalDateTime startTime;
    LocalDateTime endTime;
}

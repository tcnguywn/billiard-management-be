package com.backend.billiards_management.dtos.request.order_detail;

import lombok.Data;

import java.util.Date;

@Data
public class OrderDetailReq {
    int invoiceId;
    int quantity;
    int productId;
    Date startTime;
    Date endTime;
}

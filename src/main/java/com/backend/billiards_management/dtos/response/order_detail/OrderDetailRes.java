package com.backend.billiards_management.dtos.response.order_detail;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
public class OrderDetailRes {
    String productName;
    int quantity;
    BigDecimal unitPrice;
    BigDecimal totalPrice;
    Date startTime;
    Date endTime;
}

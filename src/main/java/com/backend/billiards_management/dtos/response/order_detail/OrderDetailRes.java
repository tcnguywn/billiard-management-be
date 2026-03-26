package com.backend.billiards_management.dtos.response.order_detail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderDetailRes {
    private String productName;
    private int quantity;
    private BigDecimal totalPrice;
}

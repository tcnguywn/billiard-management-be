package com.backend.billiards_management.dtos.response.purchase;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseDetailRes {

    private int id;

    private int productId;

    private String productName;

    private String imageUrl;

    private Integer quantity;

    private BigDecimal importPrice;
    // Tổng tiền của dòng này = quantity * importPrice (backend tính sẵn cho Frontend)
    private BigDecimal subTotal;
}
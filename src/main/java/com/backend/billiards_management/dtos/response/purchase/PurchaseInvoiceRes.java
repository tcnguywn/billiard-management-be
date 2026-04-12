package com.backend.billiards_management.dtos.response.purchase;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class PurchaseInvoiceRes {
    private int purchaseId;
    private BigDecimal totalPrice;
    private LocalDateTime purchaseDate;
}

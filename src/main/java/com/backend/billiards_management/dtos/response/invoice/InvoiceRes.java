package com.backend.billiards_management.dtos.response.invoice;

import com.backend.billiards_management.entities.invoice.enums.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceRes {
    private int id;
    private PaymentStatus status;
    private BigDecimal totalAmount;
    private Date createdAt;
    private Date updatedAt;
}

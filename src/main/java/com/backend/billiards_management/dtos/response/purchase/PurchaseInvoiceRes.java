package com.backend.billiards_management.dtos.response.purchase;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseInvoiceRes {
    private int id;

    private BigDecimal totalAmount;

    private LocalDateTime importDate;

    private int employeeId;

    private String employeeName;

    private List<PurchaseDetailRes> details;
}

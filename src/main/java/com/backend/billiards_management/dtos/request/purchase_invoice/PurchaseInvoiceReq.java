package com.backend.billiards_management.dtos.request.purchase_invoice;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseInvoiceReq {

    @NotNull(message = "MNV khong duoc de trong")
    private Integer employeeId;

    private LocalDateTime importDate;

    @NotEmpty(message = "Danh sach chi tiet khong duoc de trong")
    private List<PurchaseDetailReq> details;
}
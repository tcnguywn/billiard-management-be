package com.backend.billiards_management.dtos.request.invoice;

import com.backend.billiards_management.entities.invoice.enums.PaymentStatus;
import com.backend.billiards_management.entities.order_detail.OrderDetail;
import com.backend.billiards_management.entities.voucher.Voucher;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InvoiceDetailRes {
    private int id;

    private String tableName;
    private String playtime;
    private BigDecimal playAmount;

    private List<OrderDetail> orderDetail;
    private BigDecimal serviceAmount;
    private BigDecimal productAmount;

    private BigDecimal tempAmount;
    private BigDecimal taxAmount;
    private BigDecimal totalAmount;

    private Voucher voucher;

    private PaymentStatus status;
    private Date createdAt;
    private Date updatedAt;
}

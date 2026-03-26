package com.backend.billiards_management.entities.invoice;

import com.backend.billiards_management.entities.BaseEntity;
import com.backend.billiards_management.entities.billiard_table.BilliardTable;
import com.backend.billiards_management.entities.employee.Employee;
import com.backend.billiards_management.entities.invoice.enums.PaymentMethod;
import com.backend.billiards_management.entities.invoice.enums.PaymentStatus;
import com.backend.billiards_management.entities.order_detail.OrderDetail;
import com.backend.billiards_management.entities.voucher.Voucher;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "invoices")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invoice extends BaseEntity {

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private PaymentStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    @Column(name = "service_amount", precision = 12, scale = 2)
    private BigDecimal serviceAmount;

    @Column(name = "product_amount", precision = 12, scale = 2)
    private BigDecimal productAmount;

    @Column(name = "tax_amount", precision = 12, scale = 2)
    private BigDecimal taxAmount;

    @Column(name = "total_amount", precision = 12, scale = 2)
    private BigDecimal totalAmount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "voucher_id")
    private Voucher voucher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "billiard_table_id")
    private BilliardTable billiardTable;
}
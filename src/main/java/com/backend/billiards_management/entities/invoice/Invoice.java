package com.backend.billiards_management.entities.invoice;

import com.backend.billiards_management.entities.billiard_table.BilliardTable;
import com.backend.billiards_management.entities.employee.Employee;
import com.backend.billiards_management.entities.invoice.enums.PaymentMethod;
import com.backend.billiards_management.entities.invoice.enums.PaymentStatus;
import com.backend.billiards_management.entities.voucher.Voucher;
import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Table(name = "invoices")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Invoice {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private int id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "start_time")
    private Date startTime;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "end_time")
    private Date endTime;

    @Column(name = "status")
    private PaymentStatus status;

    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    @Column(name = "service_amount")
    private Double serviceAmount;

    @Column(name = "product_amount")
    private Double productAmount;

    @Column(name = "tax_amount")
    private Double taxAmount;

    @Column(name = "total_amount")
    private Double totalAmount;

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
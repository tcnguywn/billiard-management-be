package com.backend.billiards_management.entities.purchase_invoice;

import com.backend.billiards_management.entities.BaseEntity;
import com.backend.billiards_management.entities.employee.Employee;
import com.backend.billiards_management.entities.purchase_detail.PurchaseDetail;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "purchase_invoices")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseInvoice extends BaseEntity {

    @Column(name = "total_amount", precision = 12, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "import_date")
    private LocalDateTime importDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @OneToMany(mappedBy = "purchaseInvoice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PurchaseDetail> purchaseDetails;
}
package com.backend.billiards_management.entities.purchase_invoice;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Table(name = "purchase_invoices")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseInvoice {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private int id;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "import_date")
    private Date importDate;

    @Column(name = "total_amount")
    private Integer totalAmount;
}
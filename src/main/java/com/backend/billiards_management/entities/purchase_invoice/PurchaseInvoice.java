package com.backend.billiards_management.entities.purchase_invoice;

import com.backend.billiards_management.entities.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Table(name = "purchase_invoices")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseInvoice extends BaseEntity {

    @Column(name = "total_amount")
    private Integer totalAmount;
}
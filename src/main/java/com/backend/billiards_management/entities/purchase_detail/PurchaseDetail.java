package com.backend.billiards_management.entities.purchase_detail;

import com.backend.billiards_management.entities.product.Product;
import com.backend.billiards_management.entities.purchase_invoice.PurchaseInvoice;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "purchase_details")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PurchaseDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "import_price")
    private Integer importPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "purchase_invoice_id")
    private PurchaseInvoice purchaseInvoice;

}
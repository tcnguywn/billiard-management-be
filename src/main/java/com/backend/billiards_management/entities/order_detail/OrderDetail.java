package com.backend.billiards_management.entities.order_detail;

import com.backend.billiards_management.entities.BaseEntity;
import com.backend.billiards_management.entities.invoice.Invoice;
import com.backend.billiards_management.entities.product.Product;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(name = "order_details")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetail extends BaseEntity {

    @Column(name = "price", precision = 10, scale = 2)
    private BigDecimal price;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "note")
    private String note;

    @Column(name = "start_time")
    private LocalDateTime startTime;

    @Column(name = "end_time")
    private LocalDateTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "invoice_id")
    private Invoice invoice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;
}
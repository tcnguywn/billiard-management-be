package com.backend.billiards_management.dtos.response.order_detail;

import com.backend.billiards_management.entities.product_category.enums.ProductCategoryType;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class OrderDetailRes {
    private Integer id;

    private Integer invoiceId;

    private Integer productId;
    private String productName;

    private BigDecimal price;
    private Integer quantity;

    private String note;

    private LocalDateTime startTime;
    private LocalDateTime endTime;

    private ProductCategoryType categoryType;

    private boolean deleted;
}

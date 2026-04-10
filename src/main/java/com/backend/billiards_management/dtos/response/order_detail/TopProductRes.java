package com.backend.billiards_management.dtos.response.order_detail;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TopProductRes {

    private int productId;
    private String productName;
    private String categoryName;
    private Long totalSold;
    private BigDecimal totalRevenue;
    private BigDecimal revenuePercentage;
    //    Cần Constructor 5 tham số để map cho repo
    public TopProductRes(int productId, String productName, String categoryName, Long totalSold, BigDecimal totalRevenue) {
        this.productId = productId;
        this.productName = productName;
        this.categoryName = categoryName;
        this.totalSold = totalSold;
        this.totalRevenue = totalRevenue;
    }
}

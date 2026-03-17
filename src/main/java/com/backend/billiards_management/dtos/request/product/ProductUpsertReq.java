package com.backend.billiards_management.dtos.request.product;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductUpsertReq {
    int id;
    String name;
    BigDecimal sellingPrice;
    BigDecimal importPrice;
    int initStock;
    int imageId;
    int categoryId;
}

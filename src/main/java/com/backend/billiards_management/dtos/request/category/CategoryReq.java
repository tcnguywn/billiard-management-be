package com.backend.billiards_management.dtos.request.category;

import com.backend.billiards_management.entities.product_category.enums.ProductCategoryType;
import lombok.Data;

@Data
public class CategoryReq {

    private String categoryName;

    private ProductCategoryType type;
}

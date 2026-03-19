package com.backend.billiards_management.services.category;

import com.backend.billiards_management.entities.product_category.ProductCategory;

public interface CategoryService {
    ProductCategory getCategoryById(int id);
    ProductCategory addCategory(ProductCategory productCategory);
}

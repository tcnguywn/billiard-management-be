package com.backend.billiards_management.services.category;

import com.backend.billiards_management.dtos.request.category.CategoryReq;
import com.backend.billiards_management.entities.product_category.ProductCategory;

import java.util.List;

public interface CategoryService {
    ProductCategory getCategoryById(int id);
    ProductCategory addCategory(CategoryReq productCategory);
    List<ProductCategory> getAllCategories();
}

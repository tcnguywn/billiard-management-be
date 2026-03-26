package com.backend.billiards_management.services.category;

import com.backend.billiards_management.entities.product_category.ProductCategory;
import com.backend.billiards_management.repositories.ProductCategoryRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService{

    private final ProductCategoryRepository productCategoryRepository;

    @Override
    @Transactional
    public ProductCategory getCategoryById(int id) {
        return productCategoryRepository.findById(id).orElse(null);
    }

    @Override
    public ProductCategory addCategory(ProductCategory productCategory) {
        ProductCategory category = ProductCategory.builder()
                .categoryName(productCategory.getCategoryName())
                .type(productCategory.getType())
                .build();

        productCategoryRepository.save(category);
        return category;
    }
}

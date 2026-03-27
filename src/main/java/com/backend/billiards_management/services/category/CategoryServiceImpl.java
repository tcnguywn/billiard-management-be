package com.backend.billiards_management.services.category;

import com.backend.billiards_management.dtos.request.category.CategoryReq;
import com.backend.billiards_management.entities.product_category.ProductCategory;
import com.backend.billiards_management.repositories.ProductCategoryRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
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
    public ProductCategory addCategory(CategoryReq req) {
        ProductCategory category = ProductCategory.builder()
                .categoryName(req.getCategoryName())
                .type(req.getType())
                .build();
        log.info("Adding category: {}", category);
        productCategoryRepository.save(category);
        log.info("Category added: {}", category);
        return category;
    }

    @Override
    public List<ProductCategory> getAllCategories() {
        return productCategoryRepository.findAll();
    }
}

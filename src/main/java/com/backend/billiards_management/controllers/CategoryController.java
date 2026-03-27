package com.backend.billiards_management.controllers;

import com.backend.billiards_management.dtos.request.category.CategoryReq;
import com.backend.billiards_management.dtos.response.ApiResponse;
import com.backend.billiards_management.entities.product_category.ProductCategory;
import com.backend.billiards_management.services.category.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping()
    public ResponseEntity<ApiResponse<ProductCategory>> addCategory(@RequestBody CategoryReq productCategory) {
        return ResponseEntity.ok(
                ApiResponse.<ProductCategory>builder()
                        .status(HttpStatus.OK.value())
                        .message("Add category success")
                        .body(categoryService.addCategory(productCategory))
                        .build()
        );
    }

    @GetMapping("/{categoryId}")
    public ResponseEntity<ApiResponse<ProductCategory>> getCategoryById(@PathVariable int categoryId) {
        return ResponseEntity.ok(
                ApiResponse.<ProductCategory>builder()
                        .status(HttpStatus.OK.value())
                        .message("Get category success")
                        .body(categoryService.getCategoryById(categoryId))
                        .build()
        );
    }
    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductCategory>>> getAllCategories() {
        return ResponseEntity.ok(
                ApiResponse.<List<ProductCategory>>builder()
                        .status(HttpStatus.OK.value())
                        .message("Get all categories success")
                        .body(categoryService.getAllCategories())
                        .build()
        );
    }
}

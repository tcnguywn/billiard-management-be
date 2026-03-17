package com.backend.billiards_management.controllers;

import com.backend.billiards_management.dtos.request.product.ProductUpsertReq;
import com.backend.billiards_management.dtos.response.ApiResponse;
import com.backend.billiards_management.dtos.response.auth.AuthResponse;
import com.backend.billiards_management.dtos.response.product.ProductRes;
import com.backend.billiards_management.entities.product.Product;
import com.backend.billiards_management.services.product.ProductService;
import com.cloudinary.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping(value ="/upsert", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ProductRes>> upsertProduct(
            @RequestPart("product")ProductUpsertReq req,
            @RequestPart("image") MultipartFile imageFile
            ) {
        ProductRes saveProduct = productService.upsertProduct(req, imageFile);
        return ResponseEntity.ok(ApiResponse.<ProductRes>builder()
                .status(HttpStatus.OK.value())
                .message("upsert product success")
                .body(saveProduct)
                .build()
        );
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<ProductRes>> getProductById(@PathVariable Integer productId) {
        return ResponseEntity.ok(
                ApiResponse.<ProductRes>builder()
                        .status(HttpStatus.OK.value())
                        .message("Login success")
                        .body(productService.getProductById(productId))
                        .build()
        );
    }

    @GetMapping()
    public ResponseEntity<ApiResponse<List<ProductRes>>> getAllProducts() {

        List<ProductRes> products = productService.getAllProducts();

        return ResponseEntity.ok(
                ApiResponse.<List<ProductRes>>builder()
                        .status(HttpStatus.OK.value())
                        .message("get all products success")
                        .body(products)
                        .build()
        );
    }

    @DeleteMapping()
    public ResponseEntity<ApiResponse<String>> deleteProduct(@RequestParam Integer productId) {
        productService.deleteProduct(productId);
        return ResponseEntity.ok(ApiResponse.<String>builder()
                .status(HttpStatus.OK.value())
                .message("Delete product success")
                .build());
    }

    @GetMapping()
    public ResponseEntity<ApiResponse<List<ProductRes>>> searchProduct(@RequestBody String keyword) {
        return ResponseEntity.ok(
                ApiResponse.<List<ProductRes>>builder()
                        .status(HttpStatus.OK.value())
                        .message("search product success")
                        .body(productService.filterProducts(keyword))
                        .build()
        );
    }
}

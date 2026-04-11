package com.backend.billiards_management.controllers;

import com.backend.billiards_management.dtos.request.product.ProductUpsertReq;
import com.backend.billiards_management.dtos.response.ApiResponse;
import com.backend.billiards_management.dtos.response.auth.AuthResponse;
import com.backend.billiards_management.dtos.response.product.ProductRes;
import com.backend.billiards_management.entities.product.Product;
import com.backend.billiards_management.services.product.ProductService;
import com.cloudinary.Api;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController extends BaseController{
    private final ProductService productService;

    private static final Set<String> VALID_SORT_FIELDS = Set.of("id", "name","sellingPrice", "importPrice", "updatedAt", "createdAt");

    @PostMapping(value ="/upsert", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<ProductRes>> upsertProduct(
            @ModelAttribute ProductUpsertReq req
//            @RequestPart("image") MultipartFile imageFile
            ) {
        ProductRes saveProduct = productService.upsertProduct(req);
        return ResponseEntity.ok(ApiResponse.<ProductRes>builder()
                .status(HttpStatus.OK.value())
                .message("upsert product success")
                .body(saveProduct)
                .build()
        );
    }

//    @PostMapping(value = "/upsert", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public ResponseEntity<?> upsertProduct(
//            @RequestPart("product") String productJson,
//            @RequestPart("image") MultipartFile imageFile
//    ) throws Exception {
//
//        ProductUpsertReq req = new ObjectMapper().readValue(productJson, ProductUpsertReq.class);
//
//        return ResponseEntity.ok(productService.upsertProduct(req, imageFile));
//    }

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
    public ResponseEntity<ApiResponse<Page<ProductRes>>> getAllProducts(
            @PageableDefault(page = 0, size = 10) Pageable pageable
    ) {
//        pageable = PageableUtils.sanitize(pageable, "id", VALID_SORT_FIELDS);
        Page<ProductRes> products = productService.getAllProducts(pageable);

        return ResponseEntity.ok(
                ApiResponse.<Page<ProductRes>>builder()
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

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Page<ProductRes>>> searchProduct(@RequestParam String keyword,
                                                                       @PageableDefault(page = 0, size = 10) Pageable pageable) {
        return ResponseEntity.ok(
                ApiResponse.<Page<ProductRes>>builder()
                        .status(HttpStatus.OK.value())
                        .message("search product success")
                        .body(productService.filterProducts(keyword, pageable))
                        .build()
        );
    }
}

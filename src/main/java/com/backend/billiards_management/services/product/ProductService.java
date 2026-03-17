package com.backend.billiards_management.services.product;

import com.backend.billiards_management.dtos.request.product.ProductUpsertReq;
import com.backend.billiards_management.dtos.response.product.ProductRes;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    List<ProductRes> getAllProducts();
    ProductRes getProductById(int id);
    ProductRes upsertProduct(ProductUpsertReq req, MultipartFile imageFile);
    void deleteProduct(int id);
    List<ProductRes> filterProducts(String name);
    void reduceStock(int productId, int quantity);
}

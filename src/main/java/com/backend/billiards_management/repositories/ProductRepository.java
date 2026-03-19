package com.backend.billiards_management.repositories;

import com.backend.billiards_management.dtos.response.product.ProductRes;
import com.backend.billiards_management.entities.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    List<Product> findByDeleteFalse();
    Optional<Product> findByIdAndDeleteFalse(int id);
    Product findByName(String name);
    List<Product> findByNameContainingIgnoreCaseAndDeletedFalse(String keyword);
}

package com.backend.billiards_management.repositories;

import com.backend.billiards_management.entities.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByDeletedFalse();
    Optional<Product> findByIdAndDeletedFalse(int id);
    Product findByName(String name);
    List<Product> findByNameContainingIgnoreCaseAndDeletedFalse(String keyword);
}

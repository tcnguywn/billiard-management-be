package com.backend.billiards_management.entities.product_category;

import com.backend.billiards_management.entities.product_category.enums.ProductCategoryType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product_categories")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private int id;

    @Column(name = "category_name")
    private String categoryName;

    @Column(name = "type")
    private ProductCategoryType type;
}
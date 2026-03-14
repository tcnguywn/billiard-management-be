package com.backend.billiards_management.entities.product_category;

import com.backend.billiards_management.entities.BaseEntity;
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
public class ProductCategory extends BaseEntity {

    @Column(name = "category_name")
    private String categoryName;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private ProductCategoryType type;
}
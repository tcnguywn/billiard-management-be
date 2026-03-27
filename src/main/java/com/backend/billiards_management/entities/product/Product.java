package com.backend.billiards_management.entities.product;

import com.backend.billiards_management.entities.BaseEntity;
import com.backend.billiards_management.entities.image.UploadImage;
import com.backend.billiards_management.entities.product_category.ProductCategory;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Product extends BaseEntity {

    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "selling_price", precision = 10, scale = 2)
    private BigDecimal sellingPrice;

    @Column(name = "import_price", precision = 10, scale = 2)
    private BigDecimal importPrice;

    @Column(name = "stock")
    private Integer stock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private ProductCategory productCategory;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    private UploadImage image;
}
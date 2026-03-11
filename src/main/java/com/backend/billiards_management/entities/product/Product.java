package com.backend.billiards_management.entities.product;

import com.backend.billiards_management.entities.image.UploadImage;
import com.backend.billiards_management.entities.product_category.ProductCategory;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "name", length = 255)
    private String name;

    @Column(name = "selling_price")
    private Integer sellingPrice;

    @Column(name = "import_price")
    private Integer importPrice;

    @Column(name = "stock")
    private Integer stock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private ProductCategory productCategory;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "image_id")
    private UploadImage image;
}
package com.backend.billiards_management.dtos.request.product;

import com.backend.billiards_management.entities.image.UploadImage;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Data
public class ProductUpsertReq {
    int id;
    String name;
    BigDecimal sellingPrice;
    BigDecimal importPrice;
    int initStock;
    int imageId;
    int categoryId;
    MultipartFile image;
}

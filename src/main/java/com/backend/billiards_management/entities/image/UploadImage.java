package com.backend.billiards_management.entities.image;

import com.backend.billiards_management.entities.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "upload_images")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UploadImage extends BaseEntity {

    @Column(name = "image_url", length = 255)
    private String imageUrl;

    @Column(name = "public_id", length = 255)
    private String publicId;
}
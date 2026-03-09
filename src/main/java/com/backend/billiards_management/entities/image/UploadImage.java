package com.backend.billiards_management.entities.image;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "upload_images")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UploadImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "image_url", length = 255)
    private String imageUrl;

    @Column(name = "file_path", length = 255)
    private String filePath;
}
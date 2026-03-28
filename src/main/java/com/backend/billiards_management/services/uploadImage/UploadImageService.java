package com.backend.billiards_management.services.uploadImage;

import com.backend.billiards_management.entities.image.UploadImage;
import org.springframework.web.multipart.MultipartFile;

public interface UploadImageService {
    UploadImage uploadImageToCloudinary(MultipartFile file);
    void deleteImageFromCloudinary(String publicId);
}

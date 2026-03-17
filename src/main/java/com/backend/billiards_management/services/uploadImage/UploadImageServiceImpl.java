package com.backend.billiards_management.services.uploadImage;

import com.backend.billiards_management.configuration.CloudinaryConfig;
import com.backend.billiards_management.entities.image.UploadImage;
import com.backend.billiards_management.exceptions.AppException;
import com.backend.billiards_management.exceptions.ErrorCode;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
@AllArgsConstructor
public class UploadImageServiceImpl implements UploadImageService {

    private final CloudinaryConfig cloudinaryConfig;

    private final Cloudinary cloudinary;

    public UploadImage uploadImageToCloudinary(MultipartFile file) {
        try {
            Map uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", "uploads",
                            "resource_type", "auto"
                    )
            );

            String imageUrl = uploadResult.get("secure_url").toString();
            String publicId = uploadResult.get("public_id").toString();

            return UploadImage.builder()
                    .imageUrl(imageUrl)
                    .publicId(publicId)
                    .build();
        } catch (Exception e) {
            throw new AppException(ErrorCode.INTERNAL_ERROR,
                    "Failed to upload image to Cloudinary");
        }
    }
}

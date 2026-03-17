package com.backend.billiards_management.services.image;

import com.backend.billiards_management.dtos.response.image.UploadRes;
import com.backend.billiards_management.entities.image.UploadImage;
import com.backend.billiards_management.exceptions.AppException;
import com.backend.billiards_management.exceptions.ErrorCode;
import com.backend.billiards_management.repositories.ImageRepository;
import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UploadImageService {
    private final Cloudinary cloudinary;

    private final ImageRepository imageRepository;
    private final ModelMapper modelMapper;

    public UploadImage upload(MultipartFile file) {
        try {
            Map uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    Map.of("folder", "billiard")
            );

            String imageUrl = uploadResult.get("secure_url").toString();
            String publicId = uploadResult.get("public_id").toString();

            UploadImage image = UploadImage.builder()
                    .imageUrl(imageUrl)
                    .publicId(publicId)
                    .build();

            return imageRepository.save(image);
        } catch (IOException e) {
            throw new AppException(ErrorCode.INTERNAL_ERROR, "Failed to upload image");
        }
    }
}

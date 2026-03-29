package com.backend.billiards_management.services.product;

import com.backend.billiards_management.configuration.ModelMapperConfig;
import com.backend.billiards_management.configuration.RestTemplateConfig;
import com.backend.billiards_management.dtos.request.product.ProductUpsertReq;
import com.backend.billiards_management.dtos.response.product.ProductRes;
import com.backend.billiards_management.entities.image.UploadImage;
import com.backend.billiards_management.entities.product.Product;
import com.backend.billiards_management.entities.product_category.ProductCategory;
import com.backend.billiards_management.exceptions.AppException;
import com.backend.billiards_management.exceptions.ErrorCode;
import com.backend.billiards_management.repositories.ImageRepository;
import com.backend.billiards_management.repositories.ProductCategoryRepository;
import com.backend.billiards_management.repositories.ProductRepository;
import com.backend.billiards_management.services.uploadImage.UploadImageService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService{

    private final ProductRepository productRepository;
    private final ImageRepository imageRepository;
    private final ProductCategoryRepository productCategoryRepository;

    private final UploadImageService uploadImageService;
    private final ModelMapperConfig modelMapperConfig;
    private final RestTemplateConfig restTemplateConfig;
    private final ModelMapper modelMapper;

    @Override
    public Page<ProductRes> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable)
                .map(product -> modelMapperConfig.modelMapper().map(product, ProductRes.class));
    }

    @Override
    public ProductRes getProductById(int id) {
        Optional<Product> prod = productRepository.findByIdAndDeletedFalse(id);

        if (prod.isEmpty()) {
            throw new AppException(ErrorCode.NOT_FOUND, "Cannot find product with id: " + id);
        }

        return modelMapperConfig.modelMapper().map(prod.get(), ProductRes.class);
    }

    @Override
    @Transactional
    public ProductRes upsertProduct(ProductUpsertReq req) {
        Product product = new Product();
        if (isProductExist(req.getId())) {
            product = productRepository.findByIdAndDeletedFalse(req.getId()).get();
        }

        MultipartFile imageFile = req.getImage();

        if (req.getName() != null)
            product.setName(req.getName());
        if (req.getSellingPrice() != null) {
            product.setSellingPrice(req.getSellingPrice());
        }
        if (req.getInitStock() != 0) {
            if (product.getStock() == null) {
                product.setStock(req.getInitStock());
            }
            else product.setStock(req.getInitStock() + product.getStock());
        }

        // Thiếu trường hợp thêm mới category
        ProductCategory productCategory = productCategoryRepository.findById(req.getCategoryId())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND,
                        "Cannot find product category with id: " + req.getCategoryId()));
        if (productCategory != null) {
            product.setProductCategory(productCategory);
        }

        if (imageFile != null && !imageFile.isEmpty()) {
            UploadImage image = uploadImageService.uploadImageToCloudinary(imageFile);
            product.setImage(image);
        }
        else {
            Product finalProduct = product;
            UploadImage existingImage = imageRepository.findById(req.getImageId())
                    .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND,
                            "Cannot find image with id: " + finalProduct.getImage().getId()));
            product.setImage(existingImage);
        }
        ProductRes productRes = modelMapper.map(product, ProductRes.class);
        if(product.getImage() != null) {
            productRes.setImageUrl(product.getImage().getImageUrl());
        }
        return productRes;
    }

    // Chưa quy định logic nếu xóa hết tất cả product của 1 category thì category đó bị xóa luôn
    // Hay chỉ là ẩn đi
    @Override
    public void deleteProduct(int id) {
        Optional<Product> product = productRepository.findByIdAndDeletedFalse(id);
        if (product.isEmpty())
            throw new AppException(ErrorCode.NOT_FOUND, "Cannot find product with id: " + id);
        product.get().setDeleted(true);
        productRepository.save(product.get());
    }

    // Giới hạn filter by name
    @Override
    public List<ProductRes> filterProducts(String name) {
        List<Product> products = new ArrayList<>();
        List<ProductRes> productRes = new ArrayList<>();
        if (name == null || name.trim().isEmpty()) {
            products = productRepository.findByDeletedFalse();
            for (Product product : products) {
                productRes.add(modelMapperConfig.modelMapper().map(product, ProductRes.class));
            }
        }

        products = productRepository.findByNameContainingIgnoreCaseAndDeletedFalse(name.trim());
        for (Product product : products) {
            productRes.add(modelMapperConfig.modelMapper().map(product, ProductRes.class));
        }
        return productRes;
    }

    // Sử dụng cho hàm thanh toán
    @Override
    @Transactional
    public void reduceStock(int productId, int quantity) {
        Optional<Product> product = productRepository.findByIdAndDeletedFalse(productId);
        if (product.isEmpty())
            throw new AppException(ErrorCode.NOT_FOUND, "Cannot find product with id: " + productId);
        if (quantity > product.get().getStock())
            throw new AppException(ErrorCode.BAD_REQUEST,
                    "Quantity cannot be greater than stock: " + product.get().getStock());

        if (quantity <= 0)
            throw new AppException(ErrorCode.BAD_REQUEST,
                    "Quantity cannot be less than or equal to 0");

        product.get().setStock(product.get().getStock() - quantity);
        productRepository.save(product.get());
    }

    private boolean isProductExist(int id) {
        return productRepository.findByIdAndDeletedFalse(id).isPresent();
    }
}

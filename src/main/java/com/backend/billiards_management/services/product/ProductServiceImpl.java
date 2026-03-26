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
import org.springframework.beans.factory.annotation.Autowired;
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

    @Override
    public List<ProductRes> getAllProducts() {
        List<Product> products = productRepository.findByDeleteFalse();

        List<ProductRes> productRes = new ArrayList<>();
        for (Product product : products) {
            productRes.add(modelMapperConfig.modelMapper().map(product, ProductRes.class));
        }

        return productRes;
    }

    @Override
    public ProductRes getProductById(int id) {
        Optional<Product> prod = productRepository.findByIdAndDeleteFalse(id);

        if (prod.isEmpty()) {
            throw new AppException(ErrorCode.NOT_FOUND, "Cannot find product with id: " + id);
        }

        return modelMapperConfig.modelMapper().map(prod.get(), ProductRes.class);
    }

    @Override
    @Transactional
    public ProductRes upsertProduct(ProductUpsertReq req, MultipartFile imageFile) {
        Product product = new Product();
        if (isProductExist(req.getId())) {
            product = productRepository.findByIdAndDeleteFalse(req.getId()).get();
        }

        if (req.getName() != null)
            product.setName(req.getName());
        if (req.getSellingPrice() != null) {
            product.setSellingPrice(req.getSellingPrice());
        }
        if (req.getInitStock() != 0) {
            product.setStock(req.getInitStock() + product.getStock());
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
            UploadImage existingImage = imageRepository.findById(product.getImage().getId())
                    .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND,
                            "Cannot find image with id: " + finalProduct.getImage().getId()));
            product.setImage(existingImage);
        }

        return modelMapperConfig.modelMapper().map(productRepository.save(product), ProductRes.class);
    }

    // Chưa quy định logic nếu xóa hết tất cả product của 1 category thì category đó bị xóa luôn
    // Hay chỉ là ẩn đi
    @Override
    public void deleteProduct(int id) {
        Optional<Product> product = productRepository.findByIdAndDeleteFalse(id);
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
            products = productRepository.findByDeleteFalse();
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
        Optional<Product> product = productRepository.findByIdAndDeleteFalse(productId);
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
        return productRepository.findByIdAndDeleteFalse(id).isPresent();
    }
}

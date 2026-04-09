package com.backend.billiards_management.services.order_detail;

import com.backend.billiards_management.dtos.request.order_detail.OrderDetailReq;
import com.backend.billiards_management.dtos.request.order_detail.UpdateOrderDetailReq;
import com.backend.billiards_management.dtos.response.order_detail.OrderDetailRes;
import com.backend.billiards_management.entities.invoice.Invoice;
import com.backend.billiards_management.entities.order_detail.OrderDetail;
import com.backend.billiards_management.entities.product.Product;
import com.backend.billiards_management.entities.product_category.enums.ProductCategoryType;
import com.backend.billiards_management.exceptions.AppException;
import com.backend.billiards_management.exceptions.ErrorCode;
import com.backend.billiards_management.repositories.InvoiceRepository;
import com.backend.billiards_management.repositories.OrderDetailRepository;
import com.backend.billiards_management.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class OrderDetailServiceImpl implements OrderDetailService {

    private final OrderDetailRepository orderDetailRepository;
    private final InvoiceRepository invoiceRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public OrderDetailRes addOrderDetail(OrderDetailReq req) {
        // Validate invoice
        Invoice invoice = invoiceRepository.findById(req.getInvoiceId())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND,
                        "Cannot find invoice with id: " + req.getInvoiceId()));
        if (invoice.isDeleted()) {
            throw new AppException(ErrorCode.NOT_FOUND,
                    "Invoice with id " + req.getInvoiceId() + " has been deleted");
        }

        // Validate product
        Product product = productRepository.findByIdAndDeletedFalse(req.getProductId())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND,
                        "Cannot find product with id: " + req.getProductId()));

        int quantity = (req.getQuantity() == null || req.getQuantity() < 1) ? 1 : req.getQuantity();

        // Determine price
        BigDecimal price = product.getSellingPrice();
        if (price == null) {
            throw new AppException(ErrorCode.BAD_REQUEST, "Product price is not set");
        }

        // RENTAL vs RETAIL handling
        ProductCategoryType type = product.getProductCategory() != null
                ? product.getProductCategory().getType()
                : ProductCategoryType.RETAIL;

        LocalDateTime startTime = null;
        if (type == ProductCategoryType.RENTAL) {
            startTime = LocalDateTime.now();
        } else {
            // For RETAIL products, check and decrease stock
            Integer currentStock = product.getStock();
            if (currentStock == null) currentStock = 0;
            if (currentStock < quantity) {
                throw new AppException(ErrorCode.BAD_REQUEST,
                        "Not enough stock for product id " + req.getProductId() + ". Available: " + currentStock);
            }
            product.setStock(currentStock - quantity);
            productRepository.save(product);
        }

        OrderDetail detail = OrderDetail.builder()
                .invoice(invoice)
                .product(product)
                .price(price)
                .quantity(quantity)
                .note(req.getNote())
                .startTime(startTime)
                .build();

        OrderDetail saved = orderDetailRepository.save(detail);
        return toRes(saved);
    }

    @Override
    @Transactional
    public OrderDetailRes updateOrderDetail(UpdateOrderDetailReq req) {
        OrderDetail detail = orderDetailRepository.findById(req.getId())
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND,
                        "Cannot find order detail with id: " + req.getId()));

        if (detail.isDeleted()) {
            throw new AppException(ErrorCode.NOT_FOUND,
                    "Order detail with id " + req.getId() + " has been deleted");
        }

        Product product = detail.getProduct();
        ProductCategoryType type = product.getProductCategory() != null
                ? product.getProductCategory().getType()
                : ProductCategoryType.RETAIL;

        // Update quantity with stock adjustment for RETAIL
        if (req.getQuantity() != null) {
            if (req.getQuantity() < 1) {
                throw new AppException(ErrorCode.BAD_REQUEST, "Quantity must be at least 1");
            }

            int oldQty = detail.getQuantity() == null ? 1 : detail.getQuantity();
            int newQty = req.getQuantity();
            int delta = newQty - oldQty;

            if (type == ProductCategoryType.RETAIL && delta != 0) {
                Integer currentStock = product.getStock();
                if (currentStock == null) currentStock = 0;

                if (delta > 0) {
                    // Need more items -> decrease stock
                    if (currentStock < delta) {
                        throw new AppException(ErrorCode.BAD_REQUEST,
                                "Not enough stock to increase quantity. Need: " + delta + ", available: " + currentStock);
                    }
                    product.setStock(currentStock - delta);
                } else {
                    // Returning items -> increase stock
                    product.setStock(currentStock + Math.abs(delta));
                }
                productRepository.save(product);
            }
            detail.setQuantity(newQty);
        }

        if (req.getNote() != null) {
            detail.setNote(req.getNote());
        }

        OrderDetail saved = orderDetailRepository.save(detail);
        return toRes(saved);
    }

    @Override
    @Transactional
    public void deleteOrderDetail(int id) {
        OrderDetail detail = orderDetailRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND,
                        "Cannot find order detail with id: " + id));

        if (detail.isDeleted()) {
            return; // already deleted, idempotent
        }

        Product product = detail.getProduct();
        ProductCategoryType type = product.getProductCategory() != null
                ? product.getProductCategory().getType()
                : ProductCategoryType.RETAIL;

        // Restore stock for RETAIL items
        if (type == ProductCategoryType.RETAIL) {
            Integer currentStock = product.getStock();
            if (currentStock == null) currentStock = 0;
            int qty = detail.getQuantity() == null ? 0 : detail.getQuantity();
            product.setStock(currentStock + qty);
            productRepository.save(product);
        }

        detail.setDeleted(true);
        orderDetailRepository.save(detail);
    }

    // Mapping helper
    private OrderDetailRes toRes(OrderDetail d) {
        ProductCategoryType type = d.getProduct() != null && d.getProduct().getProductCategory() != null
                ? d.getProduct().getProductCategory().getType()
                : null;

        return OrderDetailRes.builder()
                .id(d.getId())
                .invoiceId(d.getInvoice() != null ? d.getInvoice().getId() : null)
                .productId(d.getProduct() != null ? d.getProduct().getId() : null)
                .productName(d.getProduct() != null ? d.getProduct().getName() : null)
                .price(d.getPrice())
                .quantity(d.getQuantity())
                .note(d.getNote())
                .startTime(d.getStartTime())
                .endTime(d.getEndTime())
                .categoryType(type)
                .deleted(d.isDeleted())
                .build();
    }
}

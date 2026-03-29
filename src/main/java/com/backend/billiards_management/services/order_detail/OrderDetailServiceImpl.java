package com.backend.billiards_management.services.order_detail;

import com.backend.billiards_management.configuration.ModelMapperConfig;
import com.backend.billiards_management.dtos.request.order_detail.AddOrderDetailReq;
import com.backend.billiards_management.dtos.request.order_detail.UpdateOrderDetailReq;
import com.backend.billiards_management.dtos.response.order_detail.OrderDetailRes;
import com.backend.billiards_management.entities.invoice.Invoice;
import com.backend.billiards_management.entities.order_detail.OrderDetail;
import com.backend.billiards_management.entities.product.Product;
import com.backend.billiards_management.exceptions.AppException;
import com.backend.billiards_management.exceptions.ErrorCode;
import com.backend.billiards_management.repositories.InvoiceRepository;
import com.backend.billiards_management.repositories.OrderDetailRepository;
import com.backend.billiards_management.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailServiceImpl implements OrderDetailService {

    private final InvoiceRepository invoiceRepository;
    private final ProductRepository productRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ModelMapperConfig modelMapperConfig;

    @Override
    @Transactional
    public OrderDetailRes addOrderDetail(AddOrderDetailReq req) {
        Invoice invoice = invoiceRepository.findById(req.getInvoiceId())
                .orElseThrow(() -> new AppException(ErrorCode.BAD_REQUEST,
                        "Invoice not found with id: " + req.getInvoiceId()));

        Product product = productRepository.findById(Long.valueOf(req.getProductId()))
                .orElseThrow(() -> new AppException(ErrorCode.BAD_REQUEST,
                        "Product not found with id: " + req.getProductId()));

        OrderDetail orderDetail = OrderDetail.builder()
                .price(product.getSellingPrice())
                .note(null)
                .quantity(req.getQuantity())
                .startTime(req.getStartTime() != null ? req.getStartTime() : null)
                .endTime(req.getEndTime() != null ? req.getEndTime() : null)
                .product(product)
                .build();
        orderDetail.setDeleted(false);
        orderDetail.setCreatedAt(new Date());
        orderDetail.setUpdatedAt(new Date());
        invoice.getOrderDetails().add(orderDetail);
        orderDetailRepository.save(orderDetail);

        return modelMapperConfig.modelMapper().map(orderDetail, OrderDetailRes.class);
    }

    @Override
    public OrderDetailRes updateOrderDetail(UpdateOrderDetailReq req) {
        Invoice invoice = invoiceRepository.findById(req.getInvoiceId())
                .orElseThrow(() -> new AppException(ErrorCode.BAD_REQUEST,
                        "Invoice not found with id: " + req.getInvoiceId()));

        OrderDetail orderDetail = orderDetailRepository.findById(req.getOrderDetailId())
                .orElseThrow(() -> new AppException(ErrorCode.BAD_REQUEST,
                        "Order detail not found with id: " + req.getOrderDetailId()));

        orderDetail.setQuantity(orderDetail.getQuantity() + req.getQuantity());
        invoice.getOrderDetails().stream()
                .filter(od -> od.getId() == req.getOrderDetailId())
                .findFirst()
                .ifPresent(od -> {
                    od.setQuantity(orderDetail.getQuantity());
                });
        orderDetail.setUpdatedAt(new Date());
        orderDetailRepository.save(orderDetail);

        return modelMapperConfig.modelMapper().map(orderDetail, OrderDetailRes.class);
    }

    @Override
    public OrderDetailRes getOrderDetailById(int id) {
        OrderDetail orderDetail = orderDetailRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND, "Order detail not found with id: " + id));

        return modelMapperConfig.modelMapper().map(orderDetail, OrderDetailRes.class);
    }

    @Override
    public List<OrderDetailRes> getOrderDetailsByInvoiceId(int invoiceId) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new AppException(ErrorCode.BAD_REQUEST,
                        "Invoice not found with id: " + invoiceId));

        List<OrderDetail> orderDetails = invoice.getOrderDetails();
        return orderDetails.stream()
                .map(od -> modelMapperConfig.modelMapper().map(od, OrderDetailRes.class))
                .toList();
    }

    @Override
    public void deleteOrderDetail(int id) {
        OrderDetail orderDetail = orderDetailRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND, "Order detail not found with id: " + id));

        orderDetail.setDeleted(true);
        orderDetailRepository.save(orderDetail);
    }
}

package com.backend.billiards_management.controllers;

import com.backend.billiards_management.dtos.request.order_detail.AddOrderDetailReq;
import com.backend.billiards_management.dtos.request.order_detail.UpdateOrderDetailReq;
import com.backend.billiards_management.dtos.response.ApiResponse;
import com.backend.billiards_management.dtos.response.order_detail.OrderDetailRes;
import com.backend.billiards_management.services.order_detail.OrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order-details")
public class OrderDetailController {

    private final OrderDetailService orderDetailService;

    @PostMapping
    public ResponseEntity<ApiResponse<OrderDetailRes>> addOrderDetail(@RequestBody AddOrderDetailReq req) {
        OrderDetailRes orderDetailRes = orderDetailService.addOrderDetail(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<OrderDetailRes>builder()
                        .status(HttpStatus.CREATED.value())
                        .message("Order detail added successfully")
                        .body(orderDetailRes)
                        .build()
        );
    }

    @PutMapping
    public ResponseEntity<ApiResponse<OrderDetailRes>> updateOrderDetail(@RequestBody UpdateOrderDetailReq req) {
        OrderDetailRes orderDetailRes = orderDetailService.updateOrderDetail(req);
        return ResponseEntity.ok(
                ApiResponse.<OrderDetailRes>builder()
                        .status(HttpStatus.OK.value())
                        .message("Order detail updated successfully")
                        .body(orderDetailRes)
                        .build()
        );
    }

    @GetMapping("/{orderDetailId}")
    public ResponseEntity<ApiResponse<OrderDetailRes>> getOrderDetailById(@PathVariable int orderDetailId) {
        OrderDetailRes orderDetailRes = orderDetailService.getOrderDetailById(orderDetailId);
        return ResponseEntity.ok(
                ApiResponse.<OrderDetailRes>builder()
                        .status(HttpStatus.OK.value())
                        .message("Get order detail successfully")
                        .body(orderDetailRes)
                        .build()
        );
    }

    @GetMapping("/invoice/{invoiceId}")
    public ResponseEntity<ApiResponse<List<OrderDetailRes>>> getOrderDetailsByInvoiceId(@PathVariable int invoiceId) {
        List<OrderDetailRes> orderDetails = orderDetailService.getOrderDetailsByInvoiceId(invoiceId);
        return ResponseEntity.ok(
                ApiResponse.<List<OrderDetailRes>>builder()
                        .status(HttpStatus.OK.value())
                        .message("Get order details by invoice successfully")
                        .body(orderDetails)
                        .build()
        );
    }

    @DeleteMapping("/{orderDetailId}")
    public ResponseEntity<ApiResponse<String>> deleteOrderDetail(@PathVariable int orderDetailId) {
        orderDetailService.deleteOrderDetail(orderDetailId);
        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .status(HttpStatus.OK.value())
                        .message("Order detail deleted successfully")
                        .build()
        );
    }
}

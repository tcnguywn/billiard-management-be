package com.backend.billiards_management.controllers;

import com.backend.billiards_management.dtos.request.order_detail.OrderDetailReq;
import com.backend.billiards_management.dtos.request.order_detail.UpdateOrderDetailReq;
import com.backend.billiards_management.dtos.response.ApiResponse;
import com.backend.billiards_management.dtos.response.order_detail.OrderDetailRes;
import com.backend.billiards_management.dtos.response.order_detail.TopProductRes;
import com.backend.billiards_management.services.order_detail.OrderDetailService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/v1/order-details")
@RequiredArgsConstructor
public class OrderDetailController {

    private final OrderDetailService orderDetailService;

    /**
     * Thêm một sản phẩm/dịch vụ vào hóa đơn
     * POST /api/order-details
     */
    @PostMapping
    public ResponseEntity<OrderDetailRes> addOrderDetail(
            @Valid @RequestBody OrderDetailReq req) {
        OrderDetailRes res = orderDetailService.addOrderDetail(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(res);
    }

    /**
     * Cập nhật số lượng hoặc ghi chú của một chi tiết hóa đơn
     * PUT /api/order-details
     */
    @PutMapping
    public ResponseEntity<OrderDetailRes> updateOrderDetail(
            @Valid @RequestBody UpdateOrderDetailReq req) {
        OrderDetailRes res = orderDetailService.updateOrderDetail(req);
        return ResponseEntity.ok(res);
    }

    /**
     * Xóa mềm một chi tiết hóa đơn (hoàn lại tồn kho nếu là RETAIL)
     * DELETE /api/order-details/{id}
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderDetail(@PathVariable int id) {
        orderDetailService.deleteOrderDetail(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TopProductRes>>> getTopProductByRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {

        List<TopProductRes> topProductResList = orderDetailService.getTopProductByRange(startDate, endDate);

        ApiResponse<List<TopProductRes>> apiResponse = ApiResponse.<List<TopProductRes>>builder()
                .status(HttpStatus.OK.value())
                .message("")
                .body(topProductResList)
                .build();

        return ResponseEntity.ok(apiResponse);
    }
}


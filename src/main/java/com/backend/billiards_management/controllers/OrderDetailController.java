package com.backend.billiards_management.controllers;

import com.backend.billiards_management.dtos.response.ApiResponse;
import com.backend.billiards_management.dtos.response.order_detail.TopProductRes;
import com.backend.billiards_management.services.orderDetail.OrderDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/order-detail")
public class OrderDetailController {

    private final OrderDetailService orderDetailService;

    @GetMapping()
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

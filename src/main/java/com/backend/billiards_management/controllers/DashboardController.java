package com.backend.billiards_management.controllers;

import com.backend.billiards_management.dtos.response.ApiResponse;
import com.backend.billiards_management.dtos.response.dashboard.DashboardSummaryRes;
import com.backend.billiards_management.dtos.response.dashboard.RevenueRes;
import com.backend.billiards_management.dtos.response.dashboard.enums.RevenueRange;
import com.backend.billiards_management.services.dashboard.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class DashboardController {
    private final DashboardService dashboardService;

    @GetMapping("/dashboard/summary")
    public ResponseEntity<ApiResponse<DashboardSummaryRes>> getDashboardSummary() {
        DashboardSummaryRes dashboardSummaryRes = dashboardService.getDashboardSummary();

        ApiResponse<DashboardSummaryRes> apiResponse = ApiResponse.<DashboardSummaryRes>builder()
                .status(HttpStatus.OK.value())
                .message("")
                .body(dashboardSummaryRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/dashboard/revenue")
    public ResponseEntity<ApiResponse<RevenueRes>> getRevenueChart(
            @RequestParam RevenueRange range
    ) {
        RevenueRes revenueRes = dashboardService.getRevenuesByRange(range);

        ApiResponse<RevenueRes> apiResponse = ApiResponse.<RevenueRes>builder()
                .status(HttpStatus.OK.value())
                .message("")
                .body(revenueRes)
                .build();

        return ResponseEntity.ok(apiResponse);
    }
}

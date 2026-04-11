package com.backend.billiards_management.controllers;

import com.backend.billiards_management.dtos.request.voucher.CreateVoucherRequest;
import com.backend.billiards_management.dtos.request.voucher.UpdateVoucherRequest;
import com.backend.billiards_management.dtos.response.ApiResponse;
import com.backend.billiards_management.dtos.response.voucher.VoucherResponse;
import com.backend.billiards_management.services.voucher.VoucherService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/vouchers")
public class VoucherController {
    private final VoucherService voucherService;

    @GetMapping("")
    public ResponseEntity<ApiResponse<List<VoucherResponse>>> getActiveVouchers() {
        return ResponseEntity.ok(
                ApiResponse.<List<VoucherResponse>>builder()
                        .status(200)
                        .body(voucherService.getActiveVouchers())
                        .message("Get all vouchers success")
                        .build());
    }

    @GetMapping("/{voucherId}")
    public ResponseEntity<ApiResponse<VoucherResponse>> getVoucherById(@PathVariable Long voucherId) {
        return ResponseEntity.ok(
                ApiResponse.<VoucherResponse>builder()
                        .status(200)
                        .body(voucherService.getVoucherById(voucherId))
                        .message("Get voucher success")
                        .build());
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse<VoucherResponse>> createVoucher(@RequestBody CreateVoucherRequest request) {
        return ResponseEntity.ok(
                ApiResponse.<VoucherResponse>builder()
                        .status(200)
                        .body(voucherService.createVoucher(request))
                        .message("Create voucher success")
                        .build()
        );
    }

    @PatchMapping("/{voucherId}")
    public ResponseEntity<ApiResponse<VoucherResponse>> updateVoucher(@PathVariable Long voucherId,
                                                                      @RequestBody UpdateVoucherRequest request) {
        return ResponseEntity.ok(
                ApiResponse.<VoucherResponse>builder()
                        .status(200)
                        .body(voucherService.updateVoucher(voucherId, request))
                        .message("Update voucher success")
                        .build()
        );
    }

    @GetMapping("/validate_vouchers")
    public ResponseEntity<ApiResponse<List<VoucherResponse>>> getActiveVouchers(@RequestParam BigDecimal totalBillAmount) {
        return ResponseEntity.ok(
                ApiResponse.<List<VoucherResponse>>builder()
                        .status(200)
                        .body(voucherService.validateVouchers(totalBillAmount))
                        .message("Get vouchers by amount success")
                        .build()
        );
    }

    @DeleteMapping("/{voucherId}")
    public ResponseEntity<ApiResponse<String>> deleteVoucher(@PathVariable Long voucherId) {
        voucherService.deleteVoucher(voucherId);
        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .status(200)
                        .body("Delete voucher success")
                        .message("Delete voucher success")
                        .build()
        );
    }
}

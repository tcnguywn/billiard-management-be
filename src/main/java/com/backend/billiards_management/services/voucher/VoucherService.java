package com.backend.billiards_management.services.voucher;

import com.backend.billiards_management.dtos.request.voucher.CreateVoucherRequest;
import com.backend.billiards_management.dtos.request.voucher.UpdateVoucherRequest;
import com.backend.billiards_management.dtos.response.voucher.VoucherResponse;
import com.backend.billiards_management.entities.voucher.Voucher;

import java.math.BigDecimal;
import java.util.List;

public interface VoucherService {
    VoucherResponse createVoucher(CreateVoucherRequest request);
    VoucherResponse getVoucherById(Long id);
    List<VoucherResponse> getActiveVouchers();
    VoucherResponse updateVoucher(Long id, UpdateVoucherRequest request);
    List<VoucherResponse> validateVouchers(BigDecimal totalBillAmount);

    BigDecimal calculateDiscount(Long id, BigDecimal totalBillAmount);
    void deleteVoucher(Long id);
}

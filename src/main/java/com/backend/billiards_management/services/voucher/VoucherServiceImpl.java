package com.backend.billiards_management.services.voucher;

import com.backend.billiards_management.configuration.ModelMapperConfig;
import com.backend.billiards_management.dtos.request.voucher.CreateVoucherRequest;
import com.backend.billiards_management.dtos.request.voucher.UpdateVoucherRequest;
import com.backend.billiards_management.dtos.response.voucher.VoucherResponse;
import com.backend.billiards_management.entities.voucher.Voucher;
import com.backend.billiards_management.entities.voucher.enums.VoucherSource;
import com.backend.billiards_management.entities.voucher.enums.VoucherStatus;
import com.backend.billiards_management.entities.voucher.enums.VoucherType;
import com.backend.billiards_management.exceptions.AppException;
import com.backend.billiards_management.exceptions.ErrorCode;
import com.backend.billiards_management.repositories.VoucherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class VoucherServiceImpl implements VoucherService{

    @Autowired
    private final VoucherRepository voucherRepository;
    private final ModelMapperConfig modelMapperConfig;
    @Override
    public VoucherResponse createVoucher(CreateVoucherRequest request) {
        String voucherCode = "";
        if (Objects.equals(request.getSource(), "AI"))
            voucherCode += "A";
        else if (Objects.equals(request.getSource(), "MANUAL"))
            voucherCode += "M";
        else
            throw new AppException(ErrorCode.BAD_REQUEST,
                    "Source's value must " + VoucherSource.AI + " or " + VoucherSource.MANUAL);

        if (Objects.equals(request.getVoucherType(), VoucherType.PERCENTAGE.name()))
            voucherCode += "P";
        else if (Objects.equals(request.getVoucherType(), VoucherType.CASH.name()))
            voucherCode += "C";
        else
            throw new AppException(ErrorCode.BAD_REQUEST,
                    "Voucher type's value must " + VoucherType.PERCENTAGE + " or " + VoucherType.CASH);

        if (request.getValue().compareTo(BigDecimal.ZERO) <= 0)
            throw new AppException(ErrorCode.BAD_REQUEST, "Value must be greater than 0");

        if (request.getValue().compareTo(BigDecimal.valueOf(10)) < 0)
            voucherCode += "0" + request.getValue();
        else
            voucherCode += request.getValue();

        Voucher voucher = Voucher.builder()
                .voucherCode(voucherCode + genRandomCode())
                .type(VoucherType.valueOf(request.getVoucherType()))
                .value(request.getValue())
                .source(VoucherSource.valueOf(request.getSource()))
                .status(VoucherStatus.valueOf(request.getStatus()))
                .startDate(request.getStartTime())
                .endDate(request.getEndTime())
                .quantity(request.getQuantity())
                .minimumAmount(request.getMinimumAmount())
                .maximumValue(request.getMaximumValue())
                .build();
        voucherRepository.save(voucher);

        return convertToResponse(voucher);
    }

    @Override
    public VoucherResponse getVoucherById(Long id) {
        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND, "Voucher not found"));

        return convertToResponse(voucher);
    }

    @Override
    public List<VoucherResponse> getActiveVouchers() {
        List<Voucher> vouchers = voucherRepository.findAllByStatus(VoucherStatus.ACTIVE);

        List<VoucherResponse> voucherResponses = new ArrayList<>();
        for (Voucher v : vouchers) {
            voucherResponses.add(convertToResponse(v));
        }

        return voucherResponses;
    }

    @Override
    public VoucherResponse updateVoucher(Long id, UpdateVoucherRequest request) {
        Optional<Voucher> voucher = voucherRepository.findById(id);
        if (voucher.isEmpty())
            throw new AppException(ErrorCode.NOT_FOUND, "Voucher not found");

        if (request.getStartTime() != null) {
            voucher.get().setStartDate(request.getStartTime());
            if (request.getStartTime().compareTo(voucher.get().getEndDate()) > 0)
                voucher.get().setEndDate(request.getStartTime());
        }

        if (request.getEndTime() != null) {
            if (request.getEndTime().compareTo(voucher.get().getStartDate()) < 0)
                throw new AppException(ErrorCode.BAD_REQUEST, "End time must be greater than start time");
            voucher.get().setEndDate(request.getEndTime());
        }

        if (request.getStatus() != null) {
            voucher.get().setStatus(VoucherStatus.valueOf(request.getStatus()));
        }

        if (request.getQuantity() != null) {
            voucher.get().setQuantity(request.getQuantity());
        }

        return modelMapperConfig.modelMapper().map(voucher.get(), VoucherResponse.class);
    }

    @Override
    public List<VoucherResponse> validateVouchers(BigDecimal totalBillAmount) {
        List<Voucher> vouchers = voucherRepository.findAllByStatus(VoucherStatus.ACTIVE);
        List<VoucherResponse> voucherResponses = new ArrayList<>();

        for (Voucher v : vouchers) {
            if (v.getMinimumAmount() != null && v.getMinimumAmount().compareTo(totalBillAmount) > 0)
                continue;
            voucherResponses.add(convertToResponse(v));
        }

        return voucherResponses;
    }

    @Override
    public BigDecimal calculateDiscount(Long id, BigDecimal totalBillAmount) {
        Voucher voucher = voucherRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.NOT_FOUND, "Voucher not found"));

        if (voucher.getType() == VoucherType.CASH)
            return voucher.getValue().min(voucher.getMaximumValue() != null
                    ? voucher.getMaximumValue() : voucher.getValue()).min(totalBillAmount);
        else if (voucher.getType() == VoucherType.PERCENTAGE)
            return totalBillAmount.multiply(voucher.getValue()).divide(BigDecimal.valueOf(100));
        else
            throw new AppException(ErrorCode.INTERNAL_ERROR, "Invalid voucher type");
    }

    @Override
    public void deleteVoucher(Long id) {
        Optional<Voucher> voucheropt = voucherRepository.findById(id);
        if (voucheropt.isEmpty())
            throw new AppException(ErrorCode.NOT_FOUND, "Voucher not found");
        Voucher voucher = voucheropt.get();
        voucher.setDeleted(true);
        voucherRepository.save(voucher);
    }

    private String genRandomCode() {
        String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < 3; i++) {
            int index = ThreadLocalRandom.current().nextInt(CHARACTERS.length());
            sb.append(CHARACTERS.charAt(index));
        }

        return sb.toString();
    }

    private VoucherResponse convertToResponse (Voucher voucher) {
        return VoucherResponse.builder()
                .code(voucher.getVoucherCode())
                .voucherType(voucher.getType().name())
                .value(voucher.getValue())
                .source(voucher.getSource().name())
                .status(voucher.getStatus().name())
                .startTime(voucher.getStartDate())
                .endTime(voucher.getEndDate())
                .quantity(voucher.getQuantity())
                .minimumAmount(voucher.getMinimumAmount())
                .build();
    }
}

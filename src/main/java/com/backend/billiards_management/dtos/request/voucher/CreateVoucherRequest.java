package com.backend.billiards_management.dtos.request.voucher;

import com.backend.billiards_management.entities.voucher.enums.VoucherType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateVoucherRequest {
    String voucherType;
    BigDecimal value;
    String source;
    String status;
    Date startTime;
    Date endTime;
    Integer quantity;
    BigDecimal minimumAmount;
    BigDecimal maximumValue;
}

package com.backend.billiards_management.dtos.response.voucher;

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
public class VoucherResponse {
    String code;
    String voucherType;
    BigDecimal value;
    String source;
    String status;
    Date startTime;
    Date endTime;
    Integer quantity;
    BigDecimal minimumAmount;
}

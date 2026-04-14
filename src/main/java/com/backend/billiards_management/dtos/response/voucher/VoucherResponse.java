package com.backend.billiards_management.dtos.response.voucher;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    LocalDateTime startTime;
    LocalDateTime endTime;
    Integer quantity;
    BigDecimal minimumAmount;
}

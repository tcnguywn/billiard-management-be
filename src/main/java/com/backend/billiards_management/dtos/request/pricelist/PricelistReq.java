package com.backend.billiards_management.dtos.request.pricelist;

import com.backend.billiards_management.dtos.constant.TableType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PricelistReq {

    private LocalTime startTime;

    private LocalTime endTime;

    private BigDecimal unitPrice;

    private TableType tableType;
}

package com.backend.billiards_management.dtos.response.pricelist;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalTime;

@Data
public class PricelistRes {

    private int id;

    private LocalTime startTime;

    private LocalTime endTime;

    private BigDecimal unitPrice;

    private Long tableTypeId;

    private String tableTypeName;
}
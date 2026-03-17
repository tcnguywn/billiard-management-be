package com.backend.billiards_management.dtos.response.pricelist;

import com.backend.billiards_management.dtos.constant.TableType;
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

    private TableType tableType;
}
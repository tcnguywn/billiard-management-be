package com.backend.billiards_management.dtos.response.dashboard.summary;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RevenueSummary {
    private BigDecimal todayRevenue;

    private BigDecimal lastWeekTodayRevenue;

    private Double changePercentage;
}

package com.backend.billiards_management.dtos.response.dashboard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RevenueData {
    //    Dùng String để có thể hiển thị ngày / tuần / tháng
    private String dateLabel;
    private BigDecimal revenue;
}

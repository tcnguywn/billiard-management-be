package com.backend.billiards_management.services.orderDetail;

import com.backend.billiards_management.dtos.response.order_detail.TopProductRes;
import com.backend.billiards_management.exceptions.AppException;
import com.backend.billiards_management.exceptions.ErrorCode;
import com.backend.billiards_management.repositories.OrderDetailRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailServiceImpl implements OrderDetailService {

    private final OrderDetailRepository orderDetailRepository;

    @Override
    public List<TopProductRes> getTopProductByRange(LocalDate startDate, LocalDate endDate) {

        if (startDate == null || endDate == null) {
            throw new AppException(ErrorCode.BAD_REQUEST, "Start date and end date cannot be null");
        }

        if (startDate.isAfter(endDate)) {
            throw new AppException(ErrorCode.BAD_REQUEST, "Start date cannot be after end date");
        }

        if (ChronoUnit.DAYS.between(startDate, endDate) > 30) {
            throw new AppException(ErrorCode.BAD_REQUEST, "Date range cannot exceed 30 days");
        }

        List<TopProductRes> topProductResList = orderDetailRepository.getOrderDetailsByRange(startDate, endDate.plusDays(1));

        if (topProductResList == null || topProductResList.isEmpty()) {
            return List.of();
        }

        BigDecimal totalRevenue = topProductResList.stream()
                .map(TopProductRes::getTotalRevenue)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        for (TopProductRes topProductRes : topProductResList) {
            BigDecimal percent = topProductRes.getTotalRevenue()
                    .divide(totalRevenue, 4, RoundingMode.HALF_UP)
                    .multiply(BigDecimal.valueOf(100));
            topProductRes.setRevenuePercentage(percent);
        }

        return topProductResList;
    }
}

package com.backend.billiards_management.services.orderDetail;

import com.backend.billiards_management.dtos.response.order_detail.TopProductRes;

import java.time.LocalDate;
import java.util.List;

public interface OrderDetailService {

    List<TopProductRes> getTopProductByRange(LocalDate startDate, LocalDate endDate);
}

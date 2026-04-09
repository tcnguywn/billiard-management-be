package com.backend.billiards_management.services.order_detail;

import com.backend.billiards_management.dtos.request.order_detail.OrderDetailReq;
import com.backend.billiards_management.dtos.request.order_detail.UpdateOrderDetailReq;
import com.backend.billiards_management.dtos.response.order_detail.OrderDetailRes;

public interface OrderDetailService {
    OrderDetailRes addOrderDetail(OrderDetailReq req);

    OrderDetailRes updateOrderDetail(UpdateOrderDetailReq req);

    void deleteOrderDetail(int id);
}

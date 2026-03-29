package com.backend.billiards_management.services.order_detail;

import com.backend.billiards_management.dtos.request.order_detail.AddOrderDetailReq;
import com.backend.billiards_management.dtos.request.order_detail.UpdateOrderDetailReq;
import com.backend.billiards_management.dtos.response.order_detail.OrderDetailRes;

import java.util.List;

public interface OrderDetailService {
    OrderDetailRes addOrderDetail(AddOrderDetailReq req);
    OrderDetailRes updateOrderDetail(UpdateOrderDetailReq req);
    OrderDetailRes getOrderDetailById(int id);
    List<OrderDetailRes> getOrderDetailsByInvoiceId(int invoiceId);
    void deleteOrderDetail(int id);
}

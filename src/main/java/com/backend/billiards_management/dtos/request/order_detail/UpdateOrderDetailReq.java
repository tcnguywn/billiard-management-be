package com.backend.billiards_management.dtos.request.order_detail;

import lombok.Data;

@Data
public class UpdateOrderDetailReq {
    int invoiceId;
    int quantity;
    int productId;
    int orderDetailId;
}

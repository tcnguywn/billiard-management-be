package com.backend.billiards_management.dtos.request.order_detail;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateOrderDetailReq {
    @NotNull(message = "Mã chi tiết hóa đơn không được để trống")
    private Integer id;

    @Min(value = 1, message = "Số lượng phải lớn hơn hoặc bằng 1")
    private Integer quantity;

    private String note;
}

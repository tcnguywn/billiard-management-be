package com.backend.billiards_management.dtos.request.order_detail;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderDetailReq {
    @NotNull(message = "Mã hóa đơn không được để trống")
    private Integer invoiceId;

    @NotNull(message = "Mã sản phẩm không được để trống")
    private Integer productId;

    @Min(value = 1, message = "Số lượng phải lớn hơn hoặc bằng 1")
    private Integer quantity = 1;

    // Nếu null sẽ mặc định theo giá bán của sản phẩm
    @Min(value = 0, message = "Giá không được âm")
    private BigDecimal price;

    private String note;
}

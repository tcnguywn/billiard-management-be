package com.backend.billiards_management.dtos.request.purchase_invoice;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class PurchaseDetailReq {
    @NotNull(message = "Mã sản phẩm không được để trống")
    private int productId;

    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 1, message = "Số lượng nhập phải lớn hơn 0")
    private Integer quantity;

    @NotNull(message = "Giá nhập không được để trống")
    @Min(value = 0, message = "Giá nhập không được âm")
    private BigDecimal importPrice;
}

package com.backend.billiards_management.dtos.request.pricelist;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdatePricelistReq extends PricelistReq {
    private int id;
}

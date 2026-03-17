package com.backend.billiards_management.dtos.request.billiard_table;

import lombok.Data;

@Data
public class UpdateTableReq extends BilliardTableReq{
    private int id;
}

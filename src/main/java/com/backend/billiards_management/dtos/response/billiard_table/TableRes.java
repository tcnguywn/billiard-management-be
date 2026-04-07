package com.backend.billiards_management.dtos.response.billiard_table;

import com.backend.billiards_management.entities.billiard_table.enums.TableStatus;
import lombok.Data;

@Data
public class TableRes {
    private Integer id;
    private String name;
    private TableStatus status;
    private String imageUrl;
}

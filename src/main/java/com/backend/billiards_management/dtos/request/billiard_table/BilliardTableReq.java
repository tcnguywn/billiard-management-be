package com.backend.billiards_management.dtos.request.billiard_table;

import com.backend.billiards_management.dtos.constant.TableType;
import com.backend.billiards_management.entities.billiard_table.enums.TableStatus;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class BilliardTableReq {
    private String name;
    private TableStatus status;
    private TableType tableType;
    private MultipartFile imageFile;
}

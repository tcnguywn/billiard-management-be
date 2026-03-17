package com.backend.billiards_management.services.table;

import com.backend.billiards_management.dtos.request.billiard_table.BilliardTableReq;
import com.backend.billiards_management.dtos.request.billiard_table.UpdateTableReq;
import com.backend.billiards_management.dtos.response.billiard_table.TableRes;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TableService {
    TableRes getTableById(int id);
    Page<TableRes> getTables(int page, int size);
    TableRes createTable(BilliardTableReq req);
    TableRes updateTable(UpdateTableReq req);
    void deleteTable(int id);
}

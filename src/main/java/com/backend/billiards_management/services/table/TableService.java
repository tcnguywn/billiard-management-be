package com.backend.billiards_management.services.table;

import com.backend.billiards_management.dtos.request.billiard_table.BilliardTableReq;
import com.backend.billiards_management.dtos.request.billiard_table.UpdateTableReq;
import com.backend.billiards_management.dtos.response.billiard_table.TableRes;
import com.backend.billiards_management.dtos.response.billiard_table.TableResWithInvoice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TableService {
    TableRes getTableById(int id);
    Page<TableRes> getTables(Pageable pageable);
    Page<TableResWithInvoice> getTablesWithActiveStatus(Pageable pageable);
    TableRes createTable(BilliardTableReq req);
    TableRes updateTable(UpdateTableReq req);
    void deleteTable(int id);
}

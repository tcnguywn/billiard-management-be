package com.backend.billiards_management.dtos.response.billiard_table;

import com.backend.billiards_management.dtos.constant.TableType;
import com.backend.billiards_management.dtos.response.invoice.InvoiceActiveRes;
import com.backend.billiards_management.dtos.response.invoice.InvoiceRes;
import com.backend.billiards_management.entities.billiard_table.enums.TableStatus;
import com.backend.billiards_management.entities.invoice.Invoice;
import lombok.Data;

@Data
public class TableResWithInvoice {
    private Integer id;
    private String name;
    private TableStatus status;
    private TableType tableType;
    private String imageUrl;
    private InvoiceActiveRes activeInvoice;
}

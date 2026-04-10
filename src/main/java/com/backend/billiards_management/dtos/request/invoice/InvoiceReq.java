package com.backend.billiards_management.dtos.request.invoice;

import lombok.Data;

import java.util.Date;

@Data
public class InvoiceReq {
    private Date startDate;
    private Date endDate;
    private int page;
    private int size;
}

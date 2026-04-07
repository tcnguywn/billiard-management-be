package com.backend.billiards_management.dtos.response.invoice;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class InvoiceActiveRes {
    private int id;
    private LocalDateTime startAt;
    private String employeeName;
}

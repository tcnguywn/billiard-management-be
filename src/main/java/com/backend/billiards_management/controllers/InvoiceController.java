package com.backend.billiards_management.controllers;

import com.backend.billiards_management.dtos.request.invoice.InvoiceReq;
import com.backend.billiards_management.dtos.response.ApiResponse;
import com.backend.billiards_management.dtos.response.invoice.InvoiceRes;
import com.backend.billiards_management.services.invoice.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/invoices")
public class InvoiceController {
    private final InvoiceService invoiceService;

    @GetMapping()
    public ResponseEntity<ApiResponse<Page<InvoiceRes>>> getInvoicesByRange(
            @RequestBody InvoiceReq invoiceReq
    ) {
        Page<InvoiceRes> invoiceResList = invoiceService.getInvoicesByRange(invoiceReq);

        ApiResponse<Page<InvoiceRes>> apiResponse = ApiResponse.<Page<InvoiceRes>>builder()
                .status(HttpStatus.OK.value())
                .message("")
                .body(invoiceResList)
                .build();

        return ResponseEntity.ok(apiResponse);
    }

    @GetMapping("/{invoiceId}")
    public ResponseEntity<ApiResponse<InvoiceRes>> getInvoiceById(
            @PathVariable Integer invoiceId
    ) {
        return null;
    }
}

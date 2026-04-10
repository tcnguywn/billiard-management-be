package com.backend.billiards_management.controllers;

import com.backend.billiards_management.dtos.request.invoice.CreateInvoiceReq;
import com.backend.billiards_management.dtos.request.invoice.UpdateInvoiceReq;
import com.backend.billiards_management.dtos.response.ApiResponse;
import com.backend.billiards_management.dtos.response.invoice.InvoiceRes;
import com.backend.billiards_management.services.invoice.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/invoices")
public class InvoiceController {

    private final InvoiceService invoiceService;

    @PostMapping
    public ResponseEntity<ApiResponse<InvoiceRes>> createInvoice(@RequestBody CreateInvoiceReq req) {
        InvoiceRes invoiceRes = invoiceService.createInvoice(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<InvoiceRes>builder()
                        .status(HttpStatus.CREATED.value())
                        .message("Invoice created successfully")
                        .body(invoiceRes)
                        .build()
        );
    }

    @PutMapping
    public ResponseEntity<ApiResponse<InvoiceRes>> updateInvoice(@RequestBody UpdateInvoiceReq req) {
        InvoiceRes invoiceRes = invoiceService.updateInvoice(req);
        return ResponseEntity.ok(
                ApiResponse.<InvoiceRes>builder()
                        .status(HttpStatus.OK.value())
                        .message("Invoice updated successfully")
                        .body(invoiceRes)
                        .build()
        );
    }

    @GetMapping("/{invoiceId}")
    public ResponseEntity<ApiResponse<InvoiceRes>> getInvoiceById(@PathVariable int invoiceId) {
        InvoiceRes invoiceRes = invoiceService.getInvoiceById(invoiceId);
        return ResponseEntity.ok(
                ApiResponse.<InvoiceRes>builder()
                        .status(HttpStatus.OK.value())
                        .message("Get invoice successfully")
                        .body(invoiceRes)
                        .build()
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<InvoiceRes>>> getAllInvoices() {
        List<InvoiceRes> invoices = invoiceService.getAllInvoices();
        return ResponseEntity.ok(
                ApiResponse.<List<InvoiceRes>>builder()
                        .status(HttpStatus.OK.value())
                        .message("Get all invoices successfully")
                        .body(invoices)
                        .build()
        );
    }

    @DeleteMapping("/{invoiceId}")
    public ResponseEntity<ApiResponse<String>> deleteInvoice(@PathVariable int invoiceId) {
        invoiceService.deleteInvoice(invoiceId);
        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .status(HttpStatus.OK.value())
                        .message("Invoice deleted successfully")
                        .build()
        );
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<InvoiceRes>>> getInvoicesByStatus(@PathVariable String status) {
        List<InvoiceRes> invoices = invoiceService.getInvoicesByStatus(status);
        return ResponseEntity.ok(
                ApiResponse.<List<InvoiceRes>>builder()
                        .status(HttpStatus.OK.value())
                        .message("Get invoices by status successfully")
                        .body(invoices)
                        .build()
        );
    }

    @GetMapping("/employee/{employeeId}")
    public ResponseEntity<ApiResponse<List<InvoiceRes>>> getInvoicesByEmployeeId(@PathVariable int employeeId) {
        List<InvoiceRes> invoices = invoiceService.getInvoicesByEmployeeId(employeeId);
        return ResponseEntity.ok(
                ApiResponse.<List<InvoiceRes>>builder()
                        .status(HttpStatus.OK.value())
                        .message("Get invoices by employee successfully")
                        .body(invoices)
                        .build()
        );
    }

    @GetMapping("/range")
    public ResponseEntity<ApiResponse<List<InvoiceRes>>> getInvoicesByRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        List<InvoiceRes> invoices = invoiceService.getInvoicesByRange(startDate, endDate);
        return ResponseEntity.ok(
                ApiResponse.<List<InvoiceRes>>builder()
                        .status(HttpStatus.OK.value())
                        .message("")
                        .body(invoices)
                        .build()
        );
    }
}

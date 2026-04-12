package com.backend.billiards_management.controllers;

import com.backend.billiards_management.dtos.request.purchase_invoice.PurchaseInvoiceReq;
import com.backend.billiards_management.dtos.response.ApiResponse;
import com.backend.billiards_management.dtos.response.purchase.PurchaseInvoiceRes;
import com.backend.billiards_management.dtos.response.purchase.PurchaseInvoiceResDetail;
import com.backend.billiards_management.services.purchase_invoice.PurchaseInvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/purchase-invoices")
@RequiredArgsConstructor
public class PurchaseInvoiceController {

    private final PurchaseInvoiceService purchaseInvoiceService;

    @PostMapping()
    public ResponseEntity<ApiResponse<PurchaseInvoiceResDetail>> createPurchaseInvoice(@RequestBody PurchaseInvoiceReq req) {
        return ResponseEntity.ok(
                ApiResponse.<PurchaseInvoiceResDetail>builder()
                        .status(HttpStatus.OK.value())
                        .message("Create purchase invoice success")
                        .body(purchaseInvoiceService.createPurchaseInvoice(req))
                        .build()
        );
    }

    @GetMapping("/{invoiceId}")
    public ResponseEntity<ApiResponse<PurchaseInvoiceResDetail>> getPurchaseInvoiceById(@PathVariable int invoiceId) {
        return ResponseEntity.ok(
                ApiResponse.<PurchaseInvoiceResDetail>builder()
                        .status(HttpStatus.OK.value())
                        .message("Get purchase invoice success")
                        .body(purchaseInvoiceService.getPurchaseInvoiceById(invoiceId))
                        .build()
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<PurchaseInvoiceRes>>> getAllPurchaseInvoices(@PageableDefault(page = 0, size = 10) Pageable pageable) {
        Page<PurchaseInvoiceRes> purchaseInvoices = purchaseInvoiceService.getAllPurchaseInvoices(pageable);
        return ResponseEntity.ok(
                ApiResponse.<Page<PurchaseInvoiceRes>>builder()
                        .status(HttpStatus.OK.value())
                        .message("Get all purchase invoices success")
                        .body(purchaseInvoices)
                        .build()
        );
    }
    @GetMapping("/search")
    public ResponseEntity<ApiResponse<List<PurchaseInvoiceRes>>> searchByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<PurchaseInvoiceRes> result = purchaseInvoiceService.searchByDateRange(startDate, endDate);
        return ResponseEntity.ok(
                ApiResponse.<List<PurchaseInvoiceRes>>builder()
                        .status(HttpStatus.OK.value())
                        .message("Search purchase invoices by date range success")
                        .body(result)
                        .build()
        );
    }




    @DeleteMapping()
    public ResponseEntity<ApiResponse<String>> deletePurchaseInvoice(@RequestParam int invoiceId) {
        purchaseInvoiceService.deletePurchaseInvoice(invoiceId);
        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .status(HttpStatus.OK.value())
                        .message("Delete purchase invoice success")
                        .build()
        );
    }
}

package com.backend.billiards_management.controllers;

import com.backend.billiards_management.dtos.request.purchase_invoice.PurchaseInvoiceReq;
import com.backend.billiards_management.dtos.response.ApiResponse;
import com.backend.billiards_management.dtos.response.purchase.PurchaseInvoiceRes;
import com.backend.billiards_management.services.purchase_invoice.PurchaseInvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/purchase-invoices")
@RequiredArgsConstructor
public class PurchaseInvoiceController {

    private final PurchaseInvoiceService purchaseInvoiceService;

    @PostMapping()
    public ResponseEntity<ApiResponse<PurchaseInvoiceRes>> createPurchaseInvoice(@RequestBody PurchaseInvoiceReq req) {
        return ResponseEntity.ok(
                ApiResponse.<PurchaseInvoiceRes>builder()
                        .status(HttpStatus.OK.value())
                        .message("Create purchase invoice success")
                        .body(purchaseInvoiceService.createPurchaseInvoice(req))
                        .build()
        );
    }

    @GetMapping("/{invoiceId}")
    public ResponseEntity<ApiResponse<PurchaseInvoiceRes>> getPurchaseInvoiceById(@PathVariable int invoiceId) {
        return ResponseEntity.ok(
                ApiResponse.<PurchaseInvoiceRes>builder()
                        .status(HttpStatus.OK.value())
                        .message("Get purchase invoice success")
                        .body(purchaseInvoiceService.getPurchaseInvoiceById(invoiceId))
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

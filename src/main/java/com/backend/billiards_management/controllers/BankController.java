package com.backend.billiards_management.controllers;

import com.backend.billiards_management.dtos.request.bank.CreateBankReq;
import com.backend.billiards_management.dtos.request.bank.UpdateBankReq;
import com.backend.billiards_management.dtos.response.ApiResponse;
import com.backend.billiards_management.dtos.response.bank.BankRes;
import com.backend.billiards_management.dtos.response.bank.DataObjectResponse;
import com.backend.billiards_management.dtos.response.bank.ShortVietQRRes;
import com.backend.billiards_management.services.bank.BankService;
import com.backend.billiards_management.services.bank.VietQRService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/banks")
public class BankController {

    private final BankService bankService;

    private final VietQRService vietQRService;

    @PostMapping
    public ResponseEntity<ApiResponse<BankRes>> createBank(@RequestBody CreateBankReq req) {
        BankRes bankRes = bankService.createBank(req);
        return ResponseEntity.status(HttpStatus.CREATED).body(
                ApiResponse.<BankRes>builder()
                        .status(HttpStatus.CREATED.value())
                        .message("Bank created successfully")
                        .body(bankRes)
                        .build()
        );
    }

    @PutMapping
    public ResponseEntity<ApiResponse<BankRes>> updateBank(@RequestBody UpdateBankReq req) {
        BankRes bankRes = bankService.updateBank(req);
        return ResponseEntity.ok(
                ApiResponse.<BankRes>builder()
                        .status(HttpStatus.OK.value())
                        .message("Bank updated successfully")
                        .body(bankRes)
                        .build()
        );
    }

    @GetMapping("/{bankId}")
    public ResponseEntity<ApiResponse<BankRes>> getBankById(@RequestParam Integer bankId) {
        BankRes bankRes = bankService.getBankById(bankId);
        return ResponseEntity.ok(
                ApiResponse.<BankRes>builder()
                        .status(HttpStatus.OK.value())
                        .message("Get bank successfully")
                        .body(bankRes)
                        .build()
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<BankRes>>> getAllBanks() {
        List<BankRes> banks = bankService.getAllBanks();
        return ResponseEntity.ok(
                ApiResponse.<List<BankRes>>builder()
                        .status(HttpStatus.OK.value())
                        .message("Get all banks successfully")
                        .body(banks)
                        .build()
        );
    }

    @DeleteMapping("/{bankId}")
    public ResponseEntity<ApiResponse<String>> deleteBank(@PathVariable int bankId) {
        bankService.deleteBank(bankId);
        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .status(HttpStatus.OK.value())
                        .message("Bank deleted successfully")
                        .build()
        );
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<BankRes>> getActiveBank() {
        BankRes bankRes = bankService.getActiveBank();
        return ResponseEntity.ok(
                ApiResponse.<BankRes>builder()
                        .status(HttpStatus.OK.value())
                        .message("Get active bank successfully")
                        .body(bankRes)
                        .build()
        );
    }

    @GetMapping("/vietqr/{id}")
    public ResponseEntity<ApiResponse<ShortVietQRRes>> getVietQRDataObject(@PathVariable Integer id) {
        ShortVietQRRes dataObject = vietQRService.getShortVietQR(id);
        return ResponseEntity.ok(
                ApiResponse.<ShortVietQRRes>builder()
                        .status(HttpStatus.OK.value())
                        .message("Get VietQR data object successfully")
                        .body(dataObject)
                        .build()
        );
    }
}

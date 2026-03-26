package com.backend.billiards_management.controllers;

import com.backend.billiards_management.dtos.request.billiard_table.BilliardTableReq;
import com.backend.billiards_management.dtos.request.billiard_table.UpdateTableReq;
import com.backend.billiards_management.dtos.response.ApiResponse;
import com.backend.billiards_management.dtos.response.billiard_table.TableRes;
import com.backend.billiards_management.services.table.TableService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/tables")
@RequiredArgsConstructor
public class TableController {

    private final TableService tableService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<TableRes>> createTable(@ModelAttribute BilliardTableReq req) {
        return ResponseEntity.ok(
                ApiResponse.<TableRes>builder()
                        .status(HttpStatus.OK.value())
                        .message("Create table success")
                        .body(tableService.createTable(req))
                        .build()
        );
    }

    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<TableRes>> updateTable(@ModelAttribute UpdateTableReq req) {
        return ResponseEntity.ok(
                ApiResponse.<TableRes>builder()
                        .status(HttpStatus.OK.value())
                        .message("Update table success")
                        .body(tableService.updateTable(req))
                        .build()
        );
    }

    @GetMapping("/{tableId}")
    public ResponseEntity<ApiResponse<TableRes>> getTableById(@PathVariable int tableId) {
        return ResponseEntity.ok(
                ApiResponse.<TableRes>builder()
                        .status(HttpStatus.OK.value())
                        .message("Get table success")
                        .body(tableService.getTableById(tableId))
                        .build()
        );
    }

    @GetMapping()
    public ResponseEntity<ApiResponse<Page<TableRes>>> getTables(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(
                ApiResponse.<Page<TableRes>>builder()
                        .status(HttpStatus.OK.value())
                        .message("Get tables success")
                        .body(tableService.getTables(page, size))
                        .build()
        );
    }

    @DeleteMapping()
    public ResponseEntity<ApiResponse<String>> deleteTable(@RequestParam int tableId) {
        tableService.deleteTable(tableId);
        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .status(HttpStatus.OK.value())
                        .message("Delete table success")
                        .build()
        );
    }
}

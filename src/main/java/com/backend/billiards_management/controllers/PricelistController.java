package com.backend.billiards_management.controllers;

import com.backend.billiards_management.dtos.request.pricelist.PricelistReq;
import com.backend.billiards_management.dtos.request.pricelist.UpdatePricelistReq;
import com.backend.billiards_management.dtos.response.ApiResponse;
import com.backend.billiards_management.dtos.response.pricelist.PricelistRes;
import com.backend.billiards_management.services.pricelist.PriceListService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pricelists")
@RequiredArgsConstructor
public class PricelistController extends BaseController {

    private final PriceListService priceListService;

    @PostMapping()
    public ResponseEntity<ApiResponse<PricelistRes>> createPriceList(@RequestBody PricelistReq req) {
        return ResponseEntity.ok(
                ApiResponse.<PricelistRes>builder()
                        .status(HttpStatus.OK.value())
                        .message("Create price list success")
                        .body(priceListService.createPriceList(req))
                        .build()
        );
    }

    @PutMapping()
    public ResponseEntity<ApiResponse<PricelistRes>> updatePriceList(@RequestBody UpdatePricelistReq req) {
        return ResponseEntity.ok(
                ApiResponse.<PricelistRes>builder()
                        .status(HttpStatus.OK.value())
                        .message("Update price list success")
                        .body(priceListService.updatePriceList(req))
                        .build()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<PricelistRes>> getPriceListById(@PathVariable Integer id) {
        return ResponseEntity.ok(
                ApiResponse.<PricelistRes>builder()
                        .status(HttpStatus.OK.value())
                        .message("Get price list success")
                        .body(priceListService.getPriceListById(id))
                        .build()
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<PricelistRes>>> getAllPriceLists() {
        return ResponseEntity.ok(
                ApiResponse.<List<PricelistRes>>builder()
                        .status(HttpStatus.OK.value())
                        .message("Get price lists success")
                        .body(priceListService.getAllPriceLists())
                        .build()
        );
    }

    @DeleteMapping("/{pricelistId}")
    public ResponseEntity<ApiResponse<String>> deletePriceList(@PathVariable("pricelistId") Integer pricelistId) {
        priceListService.deletePriceList(pricelistId);
        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .status(HttpStatus.OK.value())
                        .message("Delete price list success")
                        .build()
        );
    }
}

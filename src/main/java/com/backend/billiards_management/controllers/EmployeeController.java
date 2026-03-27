package com.backend.billiards_management.controllers;

import com.backend.billiards_management.dtos.request.employee.CreateEmployeeReq;
import com.backend.billiards_management.dtos.response.ApiResponse;
import com.backend.billiards_management.dtos.response.employee.EmployeeRes;
import com.backend.billiards_management.services.employee.EmployeeService;
import com.cloudinary.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping("/{employeeId}")
    public ResponseEntity<ApiResponse<EmployeeRes>> getEmployeeById( @RequestParam Integer employeeId) {
        return ResponseEntity.ok(
                ApiResponse.<EmployeeRes>builder()
                        .status(HttpStatus.OK.value())
                        .message("get employee success")
                        .body(employeeService.getEmployeeById(employeeId))
                        .build()
        );
    }


    @GetMapping()
    public ResponseEntity<ApiResponse<Page<EmployeeRes>>> getAllEmployees(
            @PageableDefault(page = 0, size = 10) Pageable pageable) {
        Page<EmployeeRes> employeeRes =  employeeService.getAllEmployees(pageable);
        return ResponseEntity.ok(
                ApiResponse.<Page<EmployeeRes>>builder()
                        .status(HttpStatus.OK.value())
                        .message("get all products success")
                        .body(employeeRes)
                        .build()
        );
    }

    @PostMapping
    public ResponseEntity<ApiResponse<EmployeeRes>> createEmployee(@RequestBody CreateEmployeeReq req) {
        return ResponseEntity.ok(
                ApiResponse.<EmployeeRes>builder()
                        .status(HttpStatus.OK.value())
                        .message("create employee success")
                        .body(employeeService.createEmployee(req))
                        .build()
        );
    }

}

package com.backend.billiards_management.controllers;

import com.backend.billiards_management.dtos.request.CreateEmployeeReq;
import com.backend.billiards_management.dtos.response.EmployeeRes;
import com.backend.billiards_management.services.employee.EmployeeService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping("/{employeeId}")
    public ResponseEntity<EmployeeRes> getEmployeeById( @RequestParam Integer employeeId) {
        return ResponseEntity.ok(employeeService.getEmployeeById(employeeId));
    }


    @GetMapping
    public ResponseEntity<List<EmployeeRes>> getAllEmployees() {
        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    @PostMapping
    public ResponseEntity<EmployeeRes> createEmployee(@RequestBody CreateEmployeeReq req) {
        return ResponseEntity.ok(employeeService.createEmployee(req));
    }

}

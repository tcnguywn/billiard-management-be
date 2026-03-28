package com.backend.billiards_management.controllers;

import com.backend.billiards_management.dtos.request.employee.CreateEmployeeReq;
import com.backend.billiards_management.dtos.request.employee.UpdateEmployeeReq;
import com.backend.billiards_management.dtos.request.employee.UpdateProfileReq;
import com.backend.billiards_management.dtos.response.ApiResponse;
import com.backend.billiards_management.dtos.response.employee.EmployeeRes;
import com.backend.billiards_management.services.employee.EmployeeService;
import com.cloudinary.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
public class EmployeeController extends BaseController{

    private final EmployeeService employeeService;

    private static final Set<String> VALID_SORT_FIELDS = Set.of("id", "name", "updatedAt", "createdAt");

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
//        pageable = PageableUtils.sanitize(pageable, "id", VALID_SORT_FIELDS);
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

    @PostMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<EmployeeRes>> updateEmployee(
            @ModelAttribute("employee") UpdateEmployeeReq req) {
        return ResponseEntity.ok(
                ApiResponse.<EmployeeRes>builder()
                        .status(HttpStatus.OK.value())
                        .message("update employee success")
                        .body(employeeService.updateEmployee(req))
                        .build()
        );
    }

    @PostMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<EmployeeRes>> updateProfileEmployee(
            @ModelAttribute("employee") UpdateProfileReq req
//            @RequestPart("image") MultipartFile imageFile
    ) {
        return ResponseEntity.ok(
                ApiResponse.<EmployeeRes>builder()
                        .status(HttpStatus.OK.value())
                        .message("update employee success")
                        .body(employeeService.updateEmployeeProfile(req))
                        .build()
        );
    }

    @GetMapping("/profile")
    public ResponseEntity<ApiResponse<EmployeeRes>> getProfile() {
        return ResponseEntity.ok(
                ApiResponse.<EmployeeRes>builder()
                        .status(HttpStatus.OK.value())
                        .message("get profile success")
                        .body(employeeService.getMyProfile())
                        .build()
        );
    }

    @DeleteMapping("/{employeeId}")
    public ResponseEntity<ApiResponse<String>> deleteEmployee(@PathVariable Integer employeeId) {
        employeeService.deleteEmployee(employeeId);
        return ResponseEntity.ok(
                ApiResponse.<String>builder()
                        .status(HttpStatus.OK.value())
                        .message("delete employee success")
                        .body(null)
                        .build()
        );
    }

}

package com.backend.billiards_management.exceptions;

import com.backend.billiards_management.dtos.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse<Object>> handleAppException(AppException ex) {

        ApiResponse<Object> response = ApiResponse.builder()
                .status(ex.getErrorCode().getStatus())
                .message(ex.getMessage())
                .body(null)
                .build();

        return new ResponseEntity<>(
                response,
                HttpStatus.valueOf(ex.getErrorCode().getStatus())
        );
    }

    @ExceptionHandler
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}
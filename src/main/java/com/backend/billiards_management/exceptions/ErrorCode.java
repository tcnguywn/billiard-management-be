package com.backend.billiards_management.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {

    BAD_REQUEST(400),
    CONFLICT(409),
    FORBIDDEN(403),
    NOT_FOUND(404),
    UNAUTHORIZED(401),
    INTERNAL_ERROR(500);
    private final int status;
}
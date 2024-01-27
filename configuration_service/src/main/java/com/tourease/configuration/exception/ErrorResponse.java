package com.tourease.configuration.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
public final class ErrorResponse {
    private String error;
    private int errorCode;
    private LocalDateTime timestamp;
    private ErrorType type;
    private List<ValidationError> validations;


    public ErrorResponse(CustomException ex) {
        this(ex.getMessage(), ex.getErrorCode().getCode(), LocalDateTime.now(), ErrorType.ERROR, new ArrayList<>());
    }

    public ErrorResponse(String error, List<ValidationError> validations) {
        this.error = error;
        this.type = ErrorType.VALIDATION;
        this.errorCode = ErrorCode.Failed.getCode();
        this.validations = validations;
        this.timestamp = LocalDateTime.now();
    }
}

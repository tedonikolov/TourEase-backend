package com.tourease.configuration.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ValidationError {
    private final String name;
    private final String error;
    private ErrorCode errorCode;

    public ValidationError(String name, String error) {
        this.name = name;
        this.error = error;
    }
}

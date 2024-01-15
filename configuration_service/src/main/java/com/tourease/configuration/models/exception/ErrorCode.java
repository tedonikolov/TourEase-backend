package com.tourease.configuration.models.exception;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    EntityNotFound(0),
    AlreadyExists(1),
    WrongCredentials(2),
    NotActive(3),
    AlreadyActive(4),
    Inactive(5);

    private final int code;

    @JsonValue // to return label in Json responses
    public int getCode() {
        return code;
    }
}


package com.tourease.configuration.models.exception;

import lombok.Getter;

@Getter
public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String[] arguments;

    /**
     * All Custom errors now have an error code that will be transformed into a string with fields that can be filled using the errorParameters.
     * @param message : Optional ( will not be shown to the user )
     * @param errorCode : Enum with the different
     * @param errorParameters : an Array of all the parameters needed for the creation of the error string in the FE
     * {@see ErrorCode}
     */
    public CustomException(String message, ErrorCode errorCode, String... errorParameters) {
        super(message);
        this.errorCode = errorCode;
        this.arguments = errorParameters;
    }
}

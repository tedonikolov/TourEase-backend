package com.tourease.configuration.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class CustomExceptionHandler {
    @ExceptionHandler({MethodArgumentNotValidException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        List<ValidationError> list = new ArrayList<>();
        for (ObjectError error :  ex.getBindingResult().getAllErrors()) {
            String fieldName;
            if(error instanceof FieldError)
                fieldName = ((FieldError) error).getField();
            else
                fieldName = error.getObjectName();
            String errorMessage = error.getDefaultMessage();
            list.add(new ValidationError(fieldName, errorMessage, ErrorCode.Failed));
        }
        ErrorResponse response = new ErrorResponse("The request could not be executed", list);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler()
    public ResponseEntity<String> handleInternalExceptions(RuntimeException ex) {
        ex.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ex.getMessage());
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomExceptions(CustomException ex) {
        return new ResponseEntity<>(new ErrorResponse(ex),HttpStatus.BAD_REQUEST);
    }
}

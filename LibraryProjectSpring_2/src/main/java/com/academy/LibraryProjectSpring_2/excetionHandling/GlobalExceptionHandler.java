package com.academy.LibraryProjectSpring_2.excetionHandling;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(BusinessNotFound.class)
    public ResponseEntity<String> BusinessNotFoundHandler(BusinessNotFound businessNotFound) {
        return new ResponseEntity<String>(businessNotFound.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> IllegalArgumentExceptionHandler(IllegalArgumentException illegalArgumentException) {
        return new ResponseEntity<String>(illegalArgumentException.getMessage(), HttpStatus.ALREADY_REPORTED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> BusinessNotFoundHandler(Exception e) {
        LinkedHashMap<String, String> error = new LinkedHashMap<>();
        error.put("Timestamp: ", String.valueOf(LocalDateTime.now()));
        error.put("Error Message: ", "Something went wrong: " + e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        List<ObjectError> errors = ex.getBindingResult().getAllErrors();
        LinkedHashMap<String, String> validationErrors = new LinkedHashMap<>();
        for (ObjectError error: errors) {
            validationErrors.put(((FieldError)error).getField(), error.getDefaultMessage());
        }
        return new ResponseEntity<>(validationErrors, HttpStatus.BAD_REQUEST);
    }
}

package com.deloitte.ads.exceptions;


import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.net.BindException;
import java.util.*;
import java.util.stream.Collectors;

@RestControllerAdvice
public class MariosExceptionHandler {

    @ExceptionHandler({EmployeeAlreadyExistException.class})
    public ResponseEntity<?> handleEmployeeAlreadyExist(
            EmployeeAlreadyExistException exception,
            WebRequest request
    ) {
        return new ResponseEntity<>(new ErrorDetails(new Date().toInstant(), exception.getMessage(), request.getDescription(false)), HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler({SelfMariosException.class})
    public ResponseEntity<?> handleSelfMarioException(
            SelfMariosException exception,
            WebRequest request
    ) {
        return new ResponseEntity<>(new ErrorDetails(new Date().toInstant(), exception.getMessage(), request.getDescription(false)), HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler({MariosNotFoundException.class})
    public ResponseEntity<?> handleMariosNotFoundException(
            MariosNotFoundException exception,
            WebRequest request
    ) {
        return new ResponseEntity<>(new ErrorDetails(new Date().toInstant(), exception.getMessage(), request.getDescription(false)), HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler({EmployeeNotFoundException.class})
    public ResponseEntity<?> handleEmployeeNotFoundException(
            EmployeeNotFoundException exception,
            WebRequest request
    ) {
        return new ResponseEntity<>(new ErrorDetails(new Date().toInstant(), exception.getMessage(), request.getDescription(false)), HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    public ResponseEntity<Map<String, List<String>>> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = new ArrayList<>();
        BindingResult bindingResult = ex.getBindingResult();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            errors.add(fieldError.getDefaultMessage());
        }
        return new ResponseEntity<>(getErrorsMap(errors), new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    private Map<String, List<String>> getErrorsMap(List<String> errors) {
        Map<String, List<String>> errorResponse = new HashMap<>();
        errorResponse.put("errors", errors);
        return errorResponse;
    }


    @ExceptionHandler({Exception.class})
    public ResponseEntity<?> handleGenericException(
            Exception exception,
            WebRequest request
    ) {
        return new ResponseEntity<>(new ErrorDetails(new Date().toInstant(), exception.getMessage(), request.getDescription(false)), HttpStatus.BAD_REQUEST);

    }
}

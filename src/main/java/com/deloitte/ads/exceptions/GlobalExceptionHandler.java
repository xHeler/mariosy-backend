package com.deloitte.ads.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({EmployeeNotFoundException.class})
    public ResponseEntity<?> handleGenericException(
            EmployeeNotFoundException exception,
            WebRequest request
    ) {
        return new ResponseEntity<>(new ErrorDetails(new Date(), exception.getMessage(), request.getDescription(false)), HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<?> handleGenericException(
            Exception exception,
            WebRequest request
    ) {
        return new ResponseEntity<>(new ErrorDetails(new Date(), exception.getMessage(), request.getDescription(false)), HttpStatus.BAD_REQUEST);

    }
}

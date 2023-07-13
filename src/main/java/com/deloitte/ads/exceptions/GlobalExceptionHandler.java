package com.deloitte.ads.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({SelfMariosException.class})
    public ResponseEntity<?> handleSelfMarioException(
            SelfMariosException exception,
            WebRequest request
    ) {
        return new ResponseEntity<>(new ErrorDetails(new Date(), exception.getMessage(), request.getDescription(false)), HttpStatus.BAD_REQUEST);

    }

    @ExceptionHandler({MariosNotFoundException.class})
    public ResponseEntity<?> handleMariosNotFoundException(
            MariosNotFoundException exception,
            WebRequest request
    ) {
        return new ResponseEntity<>(new ErrorDetails(new Date(), exception.getMessage(), request.getDescription(false)), HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler({EmployeeNotFoundException.class})
    public ResponseEntity<?> handleEmployeeNotFoundException(
            EmployeeNotFoundException exception,
            WebRequest request
    ) {
        return new ResponseEntity<>(new ErrorDetails(new Date(), exception.getMessage(), request.getDescription(false)), HttpStatus.NOT_FOUND);

    }

    @ExceptionHandler({Exception.class})
    public ResponseEntity<?> handleGenericException(
            Exception exception,
            WebRequest request
    ) {
        return new ResponseEntity<>(new ErrorDetails(new Date(), exception.getMessage(), request.getDescription(false)), HttpStatus.BAD_REQUEST);

    }
}

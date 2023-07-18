package com.deloitte.ads.exceptions;

public class EmployeeAlreadyExistException extends RuntimeException {
    //todo: add log.error to all custom exceptions
    public EmployeeAlreadyExistException(String message) {
        super(message);
    }
}

package com.deloitte.ads.factories;

import com.deloitte.ads.dto.EmployeeDto;

public class EmployeeDtoFactory {

    public static EmployeeDto createEmployeeDto(String email, String password) {
        return EmployeeDto
                .builder()
                .email(email)
                .password(password)
                .build();
    }

    public static EmployeeDto createEmployeeDto(String email, String firstName, String lastName) {
        return EmployeeDto
                .builder()
                .email(email)
                .firstName(firstName)
                .lastName(lastName)
                .build();
    }

}

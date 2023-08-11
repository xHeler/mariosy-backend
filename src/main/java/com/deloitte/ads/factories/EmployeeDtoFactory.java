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

}

package com.deloitte.ads.factories;

import com.deloitte.ads.dto.EmployeeDto;
import com.deloitte.ads.models.Employee;

import java.util.UUID;


public class EmployeeFactory {

    public static Employee createEmployee() {
        return Employee.builder().build();
    }

    public static Employee createEmployee(String firstName, String lastName, String email) {
        return Employee.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .build();
    }

    public static Employee createEmployee(EmployeeDto employeeDto) {
        return Employee
                .builder()
                .firstName(employeeDto.getFirstName())
                .lastName(employeeDto.getLastName())
                .email(employeeDto.getEmail())
                .build();
    }

    public static Employee createEmployee(String id, EmployeeDto employeeDto) {
        return Employee
                .builder()
                .id(UUID.fromString(id))
                .firstName(employeeDto.getFirstName())
                .lastName(employeeDto.getLastName())
                .email(employeeDto.getEmail())
                .build();
    }
}

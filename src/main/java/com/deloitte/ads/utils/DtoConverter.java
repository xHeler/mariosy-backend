package com.deloitte.ads.utils;

import com.deloitte.ads.dto.EmployeeDto;
import com.deloitte.ads.factories.EmployeeFactory;
import com.deloitte.ads.models.Employee;

public class DtoConverter {
    public static Employee convertToEntity(EmployeeDto employeeDto) {
        return EmployeeFactory.createEmployee(employeeDto);
    }

    public static EmployeeDto convertToDto(Employee employee) {
        return EmployeeDto
                .builder()
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .email(employee.getEmail())
                .build();
    }
}

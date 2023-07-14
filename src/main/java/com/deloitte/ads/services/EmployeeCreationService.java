package com.deloitte.ads.services;

import com.deloitte.ads.dto.EmployeeDto;
import com.deloitte.ads.factories.EmployeeFactory;
import com.deloitte.ads.models.Employee;
import com.deloitte.ads.repositories.EmployeeRepository;
import com.deloitte.ads.utils.DtoConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmployeeCreationService {

    private final EmployeeRepository employeeRepository;

    public ResponseEntity<EmployeeDto> saveEmployee(EmployeeDto employeeDto) {
        // todo: check employees with the same email
        Employee employee = EmployeeFactory.createEmployee(employeeDto);
        employeeRepository.saveEmployee(employee);
        return ResponseEntity.ok(employeeDto);
    }

    public ResponseEntity<EmployeeDto> saveEmployee(String id, EmployeeDto employeeDto) {
        Employee employee = EmployeeFactory.createEmployee(id, employeeDto);
        employeeRepository.saveEmployee(employee);
        return ResponseEntity.ok(DtoConverter.convertToDto(employee));
    }
}
package com.deloitte.ads.services;

import com.deloitte.ads.dto.EmployeeDto;
import com.deloitte.ads.exceptions.EmployeeNotFoundException;
import com.deloitte.ads.factories.EmployeeFactory;
import com.deloitte.ads.models.Employee;
import com.deloitte.ads.repositories.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeManagementService {

    private final EmployeeRepository employeeRepository;
    private final EmployeeRetrievalService retrievalService;

    public void updateEmployee(Employee employee) {
        employeeRepository.updateEmployee(employee);
    }

    public ResponseEntity<EmployeeDto> updateEmployee(String id, EmployeeDto employeeDto) {
        if (!retrievalService.isEmployeeExist(id)) {
            throw new EmployeeNotFoundException("Employee with id=" + id + " not found!");
        }
        Employee employee = EmployeeFactory.createEmployee(employeeDto);
        employeeRepository.updateEmployee(employee);
        return ResponseEntity.ok(employeeDto);
    }

    public void deleteEmployee(Employee employee) {
        employeeRepository.deleteEmployee(employee);
    }

    public ResponseEntity<Void> deleteEmployeeUsingId(String id) {
        Employee employee = retrievalService.getEmployeeById(UUID.fromString(id));
        deleteEmployee(employee);
        return ResponseEntity.ok().build();
    }
}

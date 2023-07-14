package com.deloitte.ads.services;

import com.deloitte.ads.dto.EmployeeDto;
import com.deloitte.ads.models.Employee;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeCreationService creationService;
    private final EmployeeRetrievalService retrievalService;
    private final EmployeeManagementService managementService;

    public ResponseEntity<EmployeeDto> saveEmployee(EmployeeDto employeeDto) {
        return creationService.saveEmployee(employeeDto);
    }

    public ResponseEntity<EmployeeDto> saveEmployee(String id, EmployeeDto employeeDto) {
        return creationService.saveEmployee(id, employeeDto);
    }

    public Employee getEmployeeById(UUID id) {
        return retrievalService.getEmployeeById(id);
    }

    public List<Employee> getAllEmployeesByIds(List<String> ids) {
        return retrievalService.getAllEmployeesByIds(ids);
    }

    public ResponseEntity<List<Employee>> findEmployeeByQuery(String query) {
        return retrievalService.findEmployeeByQuery(query);
    }

    public void updateEmployee(Employee employee) {
        managementService.updateEmployee(employee);
    }

    public ResponseEntity<EmployeeDto> updateEmployee(String id, EmployeeDto employeeDto) {
        return managementService.updateEmployee(id, employeeDto);
    }

    public void deleteEmployee(Employee employee) {
        managementService.deleteEmployee(employee);
    }

    public ResponseEntity<Void> deleteEmployeeUsingId(String id) {
        return managementService.deleteEmployeeUsingId(id);
    }

    public List<Employee> getAllEmployees() {
        return retrievalService.getAllEmployees();
    }
}

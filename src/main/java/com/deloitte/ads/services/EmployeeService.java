package com.deloitte.ads.services;

import com.deloitte.ads.dto.EmployeeDto;
import com.deloitte.ads.dto.TokenDto;
import com.deloitte.ads.models.Employee;
import com.deloitte.ads.utils.KeycloakAuth;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.UUID;

@Service
@Validated
@RequiredArgsConstructor
@Slf4j
public class EmployeeService {

    private final EmployeeCreationService creationService;
    private final EmployeeRetrievalService retrievalService;
    private final EmployeeDataService managementService;
    private final KeycloakAuth keycloakAuth;

    public ResponseEntity<TokenDto> saveEmployee(EmployeeDto employeeDto) {
        log.info("Saving employee: {}", employeeDto);
        creationService.saveEmployee(employeeDto);
        TokenDto tokenDto = keycloakAuth.authenticateWithKeycloak(employeeDto);
        log.info("Received token from authenticator: {}", tokenDto);
        return ResponseEntity.ok(tokenDto);
    }

    public ResponseEntity<EmployeeDto> saveEmployee(String id, EmployeeDto employeeDto) {
        log.info("Saving employee with ID: {}, data: {}", id, employeeDto);
        return creationService.saveEmployee(id, employeeDto);
    }

    public ResponseEntity<Employee> getEmployeeById(String id) {
        log.info("Fetching employee by ID: {}", id);
        UUID uuid = UUID.fromString(id);
        Employee employee = retrievalService.getEmployeeById(uuid);
        return ResponseEntity.ok(employee);
    }

    public List<Employee> getAllEmployeesByIds(List<String> ids) {
        log.info("Fetching all employees by IDs: {}", ids);
        return retrievalService.getAllEmployeesByIds(ids);
    }

    public ResponseEntity<List<Employee>> findEmployeeByQuery(String query) {
        log.info("Finding employees by query: {}", query);
        return retrievalService.findEmployeeByQuery(query);
    }

    public void updateEmployee(Employee employee) {
        log.info("Updating employee: {}", employee);
        managementService.updateEmployee(employee);
    }

    public ResponseEntity<EmployeeDto> updateEmployee(String id, EmployeeDto employeeDto) {
        log.info("Updating employee with ID: {}, data: {}", id, employeeDto);
        return managementService.updateEmployee(id, employeeDto);
    }

    public void deleteEmployee(Employee employee) {
        log.info("Deleting employee: {}", employee);
        managementService.deleteEmployee(employee);
    }

    public ResponseEntity<Void> deleteEmployeeUsingId(String id) {
        log.info("Deleting employee with ID: {}", id);
        return managementService.deleteEmployeeUsingId(id);
    }

    public List<Employee> getAllEmployees() {
        log.info("Fetching all employees");
        return retrievalService.getAllEmployees();
    }
}

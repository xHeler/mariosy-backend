package com.deloitte.ads.services;

import com.deloitte.ads.dto.EmployeeDto;
import com.deloitte.ads.exceptions.EmployeeNotFoundException;
import com.deloitte.ads.factories.EmployeeFactory;
import com.deloitte.ads.models.Employee;
import com.deloitte.ads.repositories.EmployeeRepository;
import com.deloitte.ads.utils.DtoConverter;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Data
@Service
@RequiredArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public ResponseEntity<EmployeeDto> saveEmployee(Employee employee) {
        employeeRepository.saveEmployee(employee);
        return ResponseEntity.ok(DtoConverter.convertToDto(employee));
    }

    public ResponseEntity<EmployeeDto> saveEmployee(EmployeeDto employeeDto) {
        //todo: check employees with the same email
        Employee employee = EmployeeFactory.createEmployee(employeeDto);
        employeeRepository.saveEmployee(employee);
        return ResponseEntity.ok(employeeDto);
    }

    public ResponseEntity<EmployeeDto> saveEmployee(String id, EmployeeDto employeeDto) {
        Employee employee = EmployeeFactory.createEmployee(id, employeeDto);
        employeeRepository.saveEmployee(employee);
        return ResponseEntity.ok(DtoConverter.convertToDto(employee));
    }

    public Employee getEmployeeById(UUID id) {
        Optional<Employee> employeeOptional = employeeRepository.getEmployeeById(id);
        if (employeeOptional.isPresent()) return employeeOptional.get();
        throw new EmployeeNotFoundException("Employee with id=" + id + " not exist!");
    }

    public List<Employee> getAllEmployeesByIds(List<String> ids) throws Exception {
        List<Employee> employees = new ArrayList<>();
        for (String id : ids) {
            employees.add(getEmployeeById(UUID.fromString(id)));
        }
        return employees;
    }

    public ResponseEntity<List<Employee>> findEmployeeByQuery(String query) {
        List<Employee> employees = employeeRepository.findAllEmployeesByFirstName(query);
        employees.addAll(employeeRepository.findAllEmployeesByLastName(query));
        return ResponseEntity.ok(employees);
    }

    public boolean isEmployeeExist(String id) {
        Optional<Employee> employeeOptional = employeeRepository.getEmployeeById(UUID.fromString(id));
        return employeeOptional.isPresent();
    }

    public boolean isEmployeeExist(Employee employee) {
        Optional<Employee> employeeOptional = employeeRepository.getEmployeeById(employee.getId());
        return employeeOptional.isPresent();
    }

    public void updateEmployee(Employee employee) {
        employeeRepository.updateEmployee(employee);
    }

    public ResponseEntity<EmployeeDto> updateEmployee(String id, EmployeeDto employeeDto) {
        if (!isEmployeeExist(id)) throw new EmployeeNotFoundException("Employee with id=" + id + " not found!");
        Employee employee = EmployeeFactory.createEmployee(employeeDto);
        employeeRepository.updateEmployee(employee);
        return ResponseEntity.ok(employeeDto);
    }

    public void deleteEmployee(Employee employee) {
        employeeRepository.deleteEmployee(employee);
    }

    public ResponseEntity<Void> deleteEmployeeUsingId(String id) {
        Employee employee = getEmployeeById(UUID.fromString(id));
        deleteEmployee(employee);
        return ResponseEntity.ok().build();
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.getAllEmployees();
    }

}

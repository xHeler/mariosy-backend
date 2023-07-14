package com.deloitte.ads.services;

import com.deloitte.ads.exceptions.EmployeeNotFoundException;
import com.deloitte.ads.models.Employee;
import com.deloitte.ads.repositories.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeRetrievalService {

    private final EmployeeRepository employeeRepository;

    public Employee getEmployeeById(UUID id) {
        Optional<Employee> employeeOptional = employeeRepository.getEmployeeById(id);
        if (employeeOptional.isPresent()) {
            return employeeOptional.get();
        }
        throw new EmployeeNotFoundException("Employee with id=" + id + " does not exist!");
    }

    public List<Employee> getAllEmployeesByIds(List<String> ids) {
        List<Employee> employees = new ArrayList<>();
        for (String id : ids) {
            employees.add(getEmployeeById(UUID.fromString(id)));
        }
        return employees;
    }

    public ResponseEntity<List<Employee>> findEmployeeByQuery(String query) {
        List<Employee> employees = new ArrayList<>();
        employees.addAll(employeeRepository.findAllEmployeesByFirstName(query));
        employees.addAll(employeeRepository.findAllEmployeesByLastName(query));
        return ResponseEntity.ok(employees);
    }

    public boolean isEmployeeExist(String id) {
        Optional<Employee> employeeOptional = employeeRepository.getEmployeeById(UUID.fromString(id));
        return employeeOptional.isPresent();
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.getAllEmployees();
    }

}
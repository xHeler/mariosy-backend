package com.deloitte.ads.repositories.interfaces;

import com.deloitte.ads.models.Employee;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface EmployeeRepository {
    void saveEmployee(Employee employee);

    Optional<Employee> getEmployeeById(UUID id);

    List<Employee> findAllEmployeesByFirstName(String firstName);

    void updateEmployee(Employee employee);

    void deleteEmployee(Employee employee);

    List<Employee> getAllEmployees();
}

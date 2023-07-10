package com.deloitte.ads.repositories.interfaces;

import com.deloitte.ads.models.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeRepository {
    void saveEmployee(Employee employee);

    Optional<Employee> getEmployeeById(Long id);

    void updateEmployee(Employee employee);

    void deleteEmployee(Employee employee);

    List<Employee> getAllEmployees();
}

package com.deloitte.ads.repositories;

import com.deloitte.ads.models.Employee;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface EmployeeRepository {
    void saveEmployee(Employee employee);

    Optional<Employee> getEmployeeById(UUID id);

    List<Employee> findAllEmployeesByFirstName(String firstName);

    List<Employee> findAllEmployeesByLastName(String lastName);

    boolean isEmployeeWithEmailExist(String email);

    void updateEmployee(Employee employee);

    void deleteEmployee(Employee employee);

    List<Employee> getAllEmployees();
}

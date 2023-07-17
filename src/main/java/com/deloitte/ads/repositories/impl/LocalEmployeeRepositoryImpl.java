package com.deloitte.ads.repositories.impl;

import com.deloitte.ads.models.Employee;
import com.deloitte.ads.repositories.EmployeeRepository;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@Repository
@Profile("local")
public class LocalEmployeeRepositoryImpl implements EmployeeRepository {
    private Map<UUID, Employee> employeeMap = new HashMap<>();


    @Override
    public void saveEmployee(Employee employee) {
        employeeMap.put(employee.getId(), employee);
    }

    @Override
    public Optional<Employee> getEmployeeById(UUID id) {
        return Optional.ofNullable(employeeMap.get(id));
    }

    @Override
    public List<Employee> findAllEmployeesByFirstName(String firstName) {
        Collection<Employee> employees = employeeMap.values();
        return employees.stream()
                .filter(employee ->
                        isEmployeeWithFirstNameExist(firstName, employee))
                .collect(Collectors.toList());
    }

    private static boolean isEmployeeWithFirstNameExist(String firstName, Employee employee) {
        return employee.getFirstName().toLowerCase().contains(firstName.toLowerCase());
    }

    @Override
    public List<Employee> findAllEmployeesByLastName(String lastName) {
        Collection<Employee> employees = employeeMap.values();
        return employees.stream()
                .filter(employee ->
                        isEmployeeWithLastNameExist(lastName, employee))
                .collect(Collectors.toList());
    }

    private static boolean isEmployeeWithLastNameExist(String lastName, Employee employee) {
        return employee.getLastName().toLowerCase().contains(lastName.toLowerCase());
    }

    @Override
    public boolean isEmployeeWithEmailExist(String email) {
        return employeeMap.values().stream().anyMatch(employee -> employee.getEmail().equals(email));
    }


    @Override
    public void updateEmployee(Employee employee) {
        UUID id = employee.getId();
        if (getEmployeeById(id).isPresent()) employeeMap.put(employee.getId(), employee);
    }

    @Override
    public void deleteEmployee(Employee employee) {
        employeeMap.remove(employee.getId());
    }

    @Override
    public List<Employee> getAllEmployees() {
        return new ArrayList<>(employeeMap.values());
    }
}

package com.deloitte.ads.repositories;

import com.deloitte.ads.models.Employee;
import com.deloitte.ads.repositories.interfaces.EmployeeRepository;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@Repository
public class LocalEmployeeRepository implements EmployeeRepository {
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
        return employeeMap.values().stream()
                .filter(employee ->
                        employee.getFirstName().toLowerCase().contains(firstName.toLowerCase()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Employee> findAllEmployeesByLastName(String lastName) {
        return employeeMap.values().stream()
                .filter(employee ->
                        employee.getLastName().toLowerCase().contains(lastName.toLowerCase()))
                .collect(Collectors.toList());
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

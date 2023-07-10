package com.deloitte.ads.repositories;

import com.deloitte.ads.models.Employee;
import com.deloitte.ads.repositories.interfaces.EmployeeRepository;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@NoArgsConstructor
public class LocalEmployeeRepository implements EmployeeRepository {
    private Map<Long, Employee> employeeMap = new HashMap<>();

    @Override
    public void saveEmployee(Employee employee) {
        employeeMap.put(employee.getId(), employee);
    }

    @Override
    public Optional<Employee> getEmployeeById(Long id) {
        return Optional.ofNullable(employeeMap.get(id));
    }

    @Override
    public void updateEmployee(Employee employee) {
        employeeMap.put(employee.getId(), employee);
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

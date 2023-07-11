package com.deloitte.ads.services;

import com.deloitte.ads.models.Employee;
import com.deloitte.ads.repositories.interfaces.EmployeeRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Data
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public void saveEmployee(Employee employee) {
        employeeRepository.saveEmployee(employee);
    }

    public Employee getEmployeeById(UUID id) throws Exception {
        Optional<Employee> employeeOptional = employeeRepository.getEmployeeById(id);
        if (employeeOptional.isPresent()) return employeeOptional.get();
        throw new Exception("Employee with id=" + id + "not exist!");
    }

    public List<Employee> getEmployeeByFirstName(String firstName) throws Exception {
        List<Employee> employees = employeeRepository.findAllEmployeesByFirstName(firstName);
        if (employees.isEmpty()) throw new Exception("There is not any employee with first name = " + firstName);
        return employees;
    }

    public boolean isEmployeeExist(Employee employee) {
        try {
            Employee employeeById = getEmployeeById(employee.getId());
            return true;
        } catch (Exception exception) {
            return false;
        }
    }

    public void updateEmployee(Employee employee) {
        employeeRepository.updateEmployee(employee);
    }

    public void deleteEmployee(Employee employee) {
        employeeRepository.deleteEmployee(employee);
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.getAllEmployees();
    }
}

package com.deloitte.ads.services;

import com.deloitte.ads.dto.EmployeeDto;
import com.deloitte.ads.exceptions.EmployeeNotFoundException;
import com.deloitte.ads.models.Employee;
import com.deloitte.ads.repositories.LocalEmployeeRepository;
import com.deloitte.ads.repositories.MongoEmployeeRepository;
import com.deloitte.ads.repositories.interfaces.EmployeeRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
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

    public void saveEmployee(Employee employee) {
        employeeRepository.saveEmployee(employee);
    }

    public void saveEmployee(EmployeeDto employeeDto) {
        Employee employee = Employee
                .builder()
                .firstName(employeeDto.getFirstName())
                .lastName(employeeDto.getLastName())
                .email(employeeDto.getEmail())
                .build();
        employeeRepository.saveEmployee(employee);
    }

    public void saveEmployee(String id, EmployeeDto employeeDto) {
        Employee employee = Employee
                .builder()
                .id(UUID.fromString(id))
                .firstName(employeeDto.getFirstName())
                .lastName(employeeDto.getLastName())
                .email(employeeDto.getEmail())
                .build();
        employeeRepository.saveEmployee(employee);
    }

    public Employee getEmployeeById(UUID id) throws Exception {
        Optional<Employee> employeeOptional = employeeRepository.getEmployeeById(id);
        if (employeeOptional.isPresent()) return employeeOptional.get();
        throw new EmployeeNotFoundException("Employee with id=" + id + "not exist!");
    }

    public List<Employee> getAllEmployeesByIds(List<String> ids) throws Exception {
        List<Employee> employees = new ArrayList<>();
        for (String id : ids) {
            employees.add(getEmployeeById(UUID.fromString(id)));
        }
        return employees;
    }

    public List<Employee> findEmployeeByQuery(String query) throws Exception {
        List<Employee> employees = employeeRepository.findAllEmployeesByFirstName(query);
        employees.addAll(employeeRepository.findAllEmployeesByLastName(query));
        if (employees.isEmpty()) throw new Exception("There is not any employee with query=" + query);
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

    public void updateEmployee(String id, EmployeeDto employeeDto) {
        Employee employee = Employee
                .builder()
                .id(UUID.fromString(id))
                .firstName(employeeDto.getFirstName())
                .lastName(employeeDto.getLastName())
                .email(employeeDto.getEmail())
                .build();
        employeeRepository.updateEmployee(employee);
    }

    public void deleteEmployee(Employee employee) {
        employeeRepository.deleteEmployee(employee);
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.getAllEmployees();
    }

}

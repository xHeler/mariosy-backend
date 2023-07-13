package com.deloitte.ads.controller;

import com.deloitte.ads.dto.EmployeeDto;
import com.deloitte.ads.models.Employee;
import com.deloitte.ads.services.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/employee")
@RequiredArgsConstructor
public class EmployeeController {
    private final EmployeeService employeeService;

    @GetMapping
    ResponseEntity<List<Employee>> getAllEmployees() {
        List<Employee> employees = employeeService.getAllEmployees();
        return new ResponseEntity<>(employees, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<String> deleteEmployee(@PathVariable("id") String id) {
        try {
            Employee employee = employeeService.getEmployeeById(Long.valueOf(id));
            employeeService.deleteEmployee(employee);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping
    ResponseEntity<EmployeeDto> addEmployee(@RequestBody EmployeeDto employeeDto) {
        employeeService.saveEmployee(employeeDto);
        return new ResponseEntity<>(employeeDto, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    ResponseEntity<EmployeeDto> updateEmployee(@PathVariable String id, @RequestBody EmployeeDto employeeDto) {
        try {
            employeeService.updateEmployee(id, employeeDto);
            return new ResponseEntity<>(employeeDto, HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/search")
    ResponseEntity<List<Employee>> searchEmployee(@RequestParam("q") String query) {
        try {
            List<Employee> employees = employeeService.findEmployeeByQuery(query);
            return new ResponseEntity<>(employees, HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

}

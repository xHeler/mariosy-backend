package com.deloitte.ads.controller;

import com.deloitte.ads.dto.EmployeeDto;
import com.deloitte.ads.models.Employee;
import com.deloitte.ads.services.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    ResponseEntity<List<Employee>> getAllEmployees() {
        return ResponseEntity.of(Optional.ofNullable(employeeService.getAllEmployees()));
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteEmployee(@PathVariable("id") String id) {
        return employeeService.deleteEmployeeUsingId(id);
    }

    @PostMapping
    ResponseEntity<EmployeeDto> addEmployee(@RequestBody EmployeeDto employeeDto) {
        return employeeService.saveEmployee(employeeDto);
    }

    @PutMapping("/{id}")
    ResponseEntity<EmployeeDto> updateEmployee(@PathVariable String id, @RequestBody EmployeeDto employeeDto) {
        return employeeService.updateEmployee(id, employeeDto);
    }

    @GetMapping("/search")
    ResponseEntity<List<Employee>> findEmployee(@RequestParam("q") String query) {
        return employeeService.findEmployeeByQuery(query);
    }

}

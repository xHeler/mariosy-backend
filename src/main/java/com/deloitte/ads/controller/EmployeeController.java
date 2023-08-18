package com.deloitte.ads.controller;

import com.deloitte.ads.dto.EmployeeDto;
import com.deloitte.ads.models.Employee;
import com.deloitte.ads.services.EmployeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin("http://bkwiatkowski-frontend.s3-website.eu-central-1.amazonaws.com")
@RequestMapping("/api/employee")
@RequiredArgsConstructor
@Slf4j
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    ResponseEntity<List<Employee>> getAllEmployees() {
        log.info("Fetching all employees");
        List<Employee> employees = employeeService.getAllEmployees();
        return ResponseEntity.of(Optional.ofNullable(employees));
    }

    @GetMapping("/{employeeId}")
    ResponseEntity<Employee> getEmployeeById(@PathVariable("employeeId") String employeeId) {
        log.info("Getting employee with ID: {}", employeeId);
        return employeeService.getEmployeeById(employeeId);
    }

    @DeleteMapping("/{employeeId}")
    ResponseEntity<Void> deleteEmployee(@PathVariable("employeeId") String employeeId) {
        log.info("Deleting employee with ID: {}", employeeId);
        return employeeService.deleteEmployeeUsingId(employeeId);
    }

    @PostMapping
    ResponseEntity<Employee> addEmployee(@RequestBody @Valid EmployeeDto employeeDto) {
        log.info("Adding new employee: {}", employeeDto);
        return employeeService.saveEmployee(employeeDto);
    }

    @PutMapping("/{employeeId}")
    ResponseEntity<EmployeeDto> updateEmployee(@PathVariable String employeeId, @RequestBody EmployeeDto employeeDto) {
        log.info("Updating employee with ID: {}", employeeId);
        return employeeService.updateEmployee(employeeId, employeeDto);
    }

    @GetMapping("/search")
    ResponseEntity<List<Employee>> findEmployee(@RequestParam("q") String query) {
        log.info("Searching for employees with query: {}", query);
        return employeeService.findEmployeeByQuery(query);
    }

}

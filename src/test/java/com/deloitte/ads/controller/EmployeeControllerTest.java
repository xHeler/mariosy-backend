package com.deloitte.ads.controller;

import com.deloitte.ads.models.Employee;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
public class EmployeeControllerTest {

    @Autowired
    private EmployeeController employeeController;

    @Test
    public void getAllEmployees_ShouldBeEmpty() {
        ResponseEntity<List<Employee>> response = employeeController.getAllEmployees();
        List<Employee> employees = response.getBody();

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assertions.assertEquals(employees, new ArrayList<>());
    }
}

package com.deloitte.ads;


import com.deloitte.ads.models.Employee;
import com.deloitte.ads.services.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class MariosyApplication implements CommandLineRunner {
    private final EmployeeService employeeService;

    public static void main(String[] args) {
        SpringApplication.run(MariosyApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Employee employee1 = Employee
                .builder()
                .firstName("John")
                .lastName("Smith")
                .email("j.smith@gmail.com")
                .build();
        Employee employee2 = Employee
                .builder()
                .firstName("Jane")
                .lastName("Porter")
                .email("porter@gmail.com")
                .build();
        Employee employee3 = Employee
                .builder()
                .firstName("Bob")
                .lastName("Smith")
                .email("bobbbb@gmail.com")
                .build();
        employeeService.saveEmployee(employee1);
        employeeService.saveEmployee(employee2);
        employeeService.saveEmployee(employee3);
    }
}

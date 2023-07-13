package com.deloitte.ads;


import com.deloitte.ads.models.Employee;
import com.deloitte.ads.services.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@RequiredArgsConstructor
public class MariosyApplication {

    public static void main(String[] args) {
        SpringApplication.run(MariosyApplication.class, args);
    }

}

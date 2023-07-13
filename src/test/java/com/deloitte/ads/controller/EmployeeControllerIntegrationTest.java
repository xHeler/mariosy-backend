package com.deloitte.ads.controller;

import com.deloitte.ads.dto.EmployeeDto;
import com.deloitte.ads.models.Employee;
import com.deloitte.ads.services.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerIntegrationTest {
    //todo: given, when then block in tests

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetAllEmployees() throws Exception { //todo: remove `test` word from test naming convention
        Employee employee1 = Employee
                .builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@example.com")
                .build();

        Employee employee2 = Employee
                .builder()
                .firstName("Jane")
                .lastName("Smith")
                .email("jane@example.com")
                .build();

        List<Employee> employees = Arrays.asList(employee1, employee2);

        when(employeeService.getAllEmployees()).thenReturn(employees);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/employee")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(employee1.getId().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName").value(employee1.getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].lastName").value(employee1.getLastName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email").value(employee1.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(employee2.getId().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].firstName").value(employee2.getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].lastName").value(employee2.getLastName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].email").value(employee2.getEmail()));
    }

    @Test
    public void testDeleteEmployee() throws Exception {
        Employee employee = Employee
                .builder()
                .firstName("Andrey")
                .lastName("Adams")
                .email("adams@example.com")
                .build();

        UUID employeeId = employee.getId();

        employeeService.saveEmployee(employee);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/employee/{id}", employeeId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Verify that the deleteEmployee method is called with the correct employee ID
        verify(employeeService).deleteEmployee(eq(employeeService.getEmployeeById(employeeId)));
    }

    @Test
    public void testAddEmployee() throws Exception {
        EmployeeDto employeeDto = EmployeeDto
                .builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@example.com")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(employeeDto)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(employeeDto.getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(employeeDto.getLastName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(employeeDto.getEmail()));

        // Verify that the saveEmployee method is called with the correct employeeDto
        verify(employeeService).saveEmployee(eq(employeeDto));
    }

    @Test
    public void testUpdateEmployee() throws Exception {
        UUID employeeId = UUID.randomUUID();
        EmployeeDto employeeDto = EmployeeDto
                .builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@example.com")
                .build();

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/employee/{id}", employeeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(employeeDto)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value(employeeDto.getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value(employeeDto.getLastName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.email").value(employeeDto.getEmail()));

        // Verify that the updateEmployee method is called with the correct employee ID and employeeDto
        verify(employeeService).updateEmployee(eq(employeeId.toString()), eq(employeeDto));
    }

    @Test
    public void testSearchEmployee() throws Exception {
        String query = "John";

        Employee employee1 = Employee
                .builder()
                .firstName("John")
                .lastName("Doe")
                .email("john@example.com")
                .build();

        Employee employee2 = Employee
                .builder()
                .firstName("Jane")
                .lastName("Smith")
                .email("jane@example.com")
                .build();

        List<Employee> employees = Arrays.asList(employee1, employee2);

        when(employeeService.findEmployeeByQuery(eq(query))).thenReturn((ResponseEntity<List<Employee>>) employees);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/employee/search")
                        .param("q", query)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(employee1.getId().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName").value(employee1.getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].lastName").value(employee1.getLastName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].email").value(employee1.getEmail()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(employee2.getId().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].firstName").value(employee2.getFirstName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].lastName").value(employee2.getLastName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].email").value(employee2.getEmail()));
    }

    private String asJsonString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

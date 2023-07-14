package com.deloitte.ads.controller;

import com.deloitte.ads.dto.EmployeeDto;
import com.deloitte.ads.factories.EmployeeFactory;
import com.deloitte.ads.models.Employee;
import com.deloitte.ads.services.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(EmployeeController.class)
public class EmployeeControllerIntegrationTest {

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private MockMvc mockMvc;


    private final UUID id = UUID.randomUUID();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void should_ReturnListOfEmployees_When_GetAllEmployees() throws Exception {
        // Given
        List<Employee> employees = createEmployees();
        UUID johnId = employees.get(0).getId();
        UUID janeId = employees.get(1).getId();

        when(employeeService.getAllEmployees()).thenReturn(employees);

        // When & Then
        mockMvc.perform(get("/api/employee"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(johnId.toString()))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"))
                .andExpect(jsonPath("$[1].id").value(janeId.toString()))
                .andExpect(jsonPath("$[1].firstName").value("Jane"))
                .andExpect(jsonPath("$[1].lastName").value("Smith"));

        // Verify
        verify(employeeService).getAllEmployees();
    }

    @Test
    void should_ReturnNoContent_When_DeleteEmployee() throws Exception {
        // Given
        String id = "1";
        when(employeeService.deleteEmployeeUsingId(id)).thenReturn(ResponseEntity.noContent().build());

        // When & Then
        mockMvc.perform(delete("/api/employee/{id}", id))
                .andExpect(status().isNoContent());

        // Verify
        verify(employeeService).deleteEmployeeUsingId(id);
    }

    @Test
    void should_ReturnCreatedEmployee_When_AddEmployee() throws Exception {
        // Given
        EmployeeDto employeeDto = EmployeeDto.builder().firstName("John").lastName("Doe").build();
        when(employeeService.saveEmployee(any(EmployeeDto.class)))
                .thenReturn(ResponseEntity.ok(employeeDto));

        // When & Then
        mockMvc.perform(post("/api/employee")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));

        // Verify
        verify(employeeService).saveEmployee(any(EmployeeDto.class));
    }

    @Test
    void should_ReturnUpdatedEmployee_When_UpdateEmployee() throws Exception {
        // Given
        EmployeeDto employeeDto = EmployeeDto.builder().firstName("John").lastName("Doe").build();
        when(employeeService.updateEmployee(String.valueOf(id), employeeDto))
                .thenReturn(ResponseEntity.ok(employeeDto));

        // When & Then
        mockMvc.perform(put("/api/employee/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));

        // Verify
        verify(employeeService).updateEmployee(String.valueOf(id), employeeDto);
    }

    @Test
    void should_ReturnListOfEmployees_When_FindEmployee() throws Exception {
        // Given
        String query = "John";
        List<Employee> employees = createEmployees();
        UUID johnId = employees.get(0).getId();
        UUID johnnyId = employees.get(1).getId();

        when(employeeService.findEmployeeByQuery(query)).thenReturn(ResponseEntity.ok(employees));

        // When & Then
        mockMvc.perform(get("/api/employee/search")
                        .param("q", query))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(johnId.toString()))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"))
                .andExpect(jsonPath("$[1].id").value(johnnyId.toString()))
                .andExpect(jsonPath("$[1].firstName").value("Johnny"))
                .andExpect(jsonPath("$[1].lastName").value("Smith"));

        // Verify
        verify(employeeService).findEmployeeByQuery(query);
    }

    private List<Employee> createEmployees() {
        return List.of(
                EmployeeFactory.createEmployee("John", "Doe"),
                EmployeeFactory.createEmployee("Johnny", "Smith")
        );
    }
}

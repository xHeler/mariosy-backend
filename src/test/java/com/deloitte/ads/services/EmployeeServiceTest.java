package com.deloitte.ads.services;

import com.deloitte.ads.dto.EmployeeDto;
import com.deloitte.ads.models.Employee;
import com.deloitte.ads.utils.DtoConverter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class EmployeeServiceTest {

    @Mock
    private EmployeeCreationService creationService;

    @Mock
    private EmployeeRetrievalService retrievalService;

    @Mock
    private EmployeeDataService managementService;

    @InjectMocks
    private EmployeeService employeeService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void should_ReturnCreatedEmployee_When_SaveEmployee() {
        // Given
        Employee employee = Employee.builder().build();
        EmployeeDto employeeDto = DtoConverter.convertToDto(employee);
        ResponseEntity<Employee> expectedResponse = new ResponseEntity<>(employee, HttpStatus.CREATED);
        when(creationService.saveEmployee(employeeDto)).thenReturn(expectedResponse);

        // When
        ResponseEntity<Employee> response = employeeService.saveEmployee(employeeDto);

        // Then
        assertEquals(expectedResponse, response);
        verify(creationService).saveEmployee(employeeDto);
    }

    @Test
    void should_ReturnCreatedEmployee_When_SaveEmployeeWithId() {
        // Given
        String id = UUID.randomUUID().toString();
        EmployeeDto employeeDto = EmployeeDto.builder().build();
        ResponseEntity<EmployeeDto> expectedResponse = new ResponseEntity<>(employeeDto, HttpStatus.CREATED);
        when(creationService.saveEmployee(id, employeeDto)).thenReturn(expectedResponse);

        // When
        ResponseEntity<EmployeeDto> response = employeeService.saveEmployee(id, employeeDto);

        // Then
        assertEquals(expectedResponse, response);
        verify(creationService).saveEmployee(id, employeeDto);
    }

    @Test
    void should_ReturnEmployee_When_GetEmployeeById() {
        // Given
        UUID employeeId = UUID.randomUUID();
        Employee expectedEmployee = new Employee(/* initialize with necessary values */);
        when(retrievalService.getEmployeeById(employeeId)).thenReturn(expectedEmployee);

        // When
        Employee actualEmployee = employeeService.getEmployeeById(employeeId.toString()).getBody();

        // Then
        assertEquals(expectedEmployee, actualEmployee);
        verify(retrievalService).getEmployeeById(employeeId);
    }

    @Test
    void should_ReturnListOfEmployees_When_GetAllEmployeesByIds() {
        // Given
        List<String> employeeIds = Arrays.asList("1", "2", "3");
        List<Employee> expectedEmployees = List.of(new Employee(/* initialize with necessary values */));
        when(retrievalService.getAllEmployeesByIds(employeeIds)).thenReturn(expectedEmployees);

        // When
        List<Employee> actualEmployees = employeeService.getAllEmployeesByIds(employeeIds);

        // Then
        assertEquals(expectedEmployees, actualEmployees);
        verify(retrievalService).getAllEmployeesByIds(employeeIds);
    }

    @Test
    void should_ReturnListOfEmployees_When_FindEmployeeByQuery() {
        // Given
        String query = "John";
        List<Employee> expectedEmployees = List.of(new Employee(/* initialize with necessary values */));
        when(retrievalService.findEmployeeByQuery(query)).thenReturn(ResponseEntity.ok(expectedEmployees));

        // When
        ResponseEntity<List<Employee>> response = employeeService.findEmployeeByQuery(query);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedEmployees, response.getBody());
        verify(retrievalService).findEmployeeByQuery(query);
    }

    @Test
    void should_CallManagementService_When_UpdateEmployee() {
        // Given
        Employee employee = new Employee(/* initialize with necessary values */);

        // When
        employeeService.updateEmployee(employee);

        // Then
        verify(managementService).updateEmployee(employee);
    }

    @Test
    void should_ReturnUpdatedEmployee_When_UpdateEmployeeById() {
        // Given
        String employeeId = UUID.randomUUID().toString();
        EmployeeDto employeeDto = EmployeeDto.builder().build();
        ResponseEntity<EmployeeDto> expectedResponse = new ResponseEntity<>(employeeDto, HttpStatus.OK);
        when(managementService.updateEmployee(employeeId, employeeDto)).thenReturn(expectedResponse);

        // When
        ResponseEntity<EmployeeDto> response = employeeService.updateEmployee(employeeId, employeeDto);

        // Then
        assertEquals(expectedResponse, response);
        verify(managementService).updateEmployee(employeeId, employeeDto);
    }

    @Test
    void should_CallManagementService_When_DeleteEmployee() {
        // Given
        Employee employee = new Employee(/* initialize with necessary values */);

        // When
        employeeService.deleteEmployee(employee);

        // Then
        verify(managementService).deleteEmployee(employee);
    }

    @Test
    void should_ReturnOkResponse_When_DeleteEmployeeUsingId() {
        // Given
        String employeeId = "12345";
        ResponseEntity<Void> expectedResponse = new ResponseEntity<>(HttpStatus.OK);
        when(managementService.deleteEmployeeUsingId(employeeId)).thenReturn(expectedResponse);

        // When
        ResponseEntity<Void> response = employeeService.deleteEmployeeUsingId(employeeId);

        // Then
        assertEquals(expectedResponse, response);
        verify(managementService).deleteEmployeeUsingId(employeeId);
    }

    @Test
    void should_ReturnListOfEmployees_When_GetAllEmployees() {
        // Given
        List<Employee> expectedEmployees = List.of(new Employee(/* initialize with necessary values */));
        when(retrievalService.getAllEmployees()).thenReturn(expectedEmployees);

        // When
        List<Employee> actualEmployees = employeeService.getAllEmployees();

        // Then
        assertEquals(expectedEmployees, actualEmployees);
        verify(retrievalService).getAllEmployees();
    }
}

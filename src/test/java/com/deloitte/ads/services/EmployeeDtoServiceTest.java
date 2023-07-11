package com.deloitte.ads.services;

import com.deloitte.ads.models.Employee;
import com.deloitte.ads.repositories.interfaces.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeDtoServiceTest {
    @Mock
    private EmployeeRepository employeeRepository;

    private EmployeeService employeeService;

    private Employee employee;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        employeeService = new EmployeeService(employeeRepository);
        employee = Employee.builder()
                .firstName("Andy")
                .lastName("Tate")
                .email("andy@gmail.com")
                .build();
    }

    @Test
    void saveEmployee_ShouldCallRepositorySaveEmployee() {
        // Act
        employeeService.saveEmployee(employee);

        // Assert
        verify(employeeRepository, times(1)).saveEmployee(employee);
    }

    @Test
    void getEmployeeById_WhenEmployeeExists_ShouldReturnEmployee() throws Exception {
        // Arrange
        UUID id = employee.getId();
        Optional<Employee> employeeOptional = Optional.of(employee);
        when(employeeRepository.getEmployeeById(id)).thenReturn(employeeOptional);

        // Act
        Employee result = employeeService.getEmployeeById(id);

        // Assert
        assertEquals(employee, result);
    }

    @Test
    void getEmployeeById_WhenEmployeeDoesNotExist_ShouldThrowException() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(employeeRepository.getEmployeeById(id)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(Exception.class, () -> employeeService.getEmployeeById(id));
    }

    @Test
    void getEmployeeByFirstName_WhenEmployeesExist_ShouldReturnListOfEmployees() throws Exception {
        // Arrange
        String firstName = "Andy";
        Employee employee2 = Employee.builder()
                .firstName("Andy")
                .lastName("Smith")
                .email("smith@gmail.com")
                .build();
        List<Employee> employees = Arrays.asList(employee, employee2);
        when(employeeRepository.findAllEmployeesByFirstName(firstName)).thenReturn(employees);

        // Act
        List<Employee> result = employeeService.getEmployeeByFirstName(firstName);

        // Assert
        assertEquals(employees, result);
    }

    @Test
    void getEmployeeByFirstName_WhenNoEmployeesExist_ShouldThrowException() {
        // Arrange
        String firstName = "Andy";
        when(employeeRepository.findAllEmployeesByFirstName(firstName)).thenReturn(Collections.emptyList());

        // Act and Assert
        assertThrows(Exception.class, () -> employeeService.getEmployeeByFirstName(firstName));
    }

    @Test
    void isEmployeeExist_WhenEmployeeExists_ShouldReturnTrue() throws Exception {
        // Arrange
        UUID id = employee.getId();
        when(employeeRepository.getEmployeeById(id)).thenReturn(Optional.of(employee));

        // Act
        boolean result = employeeService.isEmployeeExist(employee);

        // Assert
        assertTrue(result);
    }

    @Test
    void isEmployeeExist_WhenEmployeeDoesNotExist_ShouldReturnFalse() throws Exception {
        // Arrange
        UUID id = employee.getId();
        when(employeeRepository.getEmployeeById(id)).thenReturn(Optional.empty());

        // Act
        boolean result = employeeService.isEmployeeExist(employee);

        // Assert
        assertFalse(result);
    }

    @Test
    void updateEmployee_ShouldCallRepositoryUpdateEmployee() {
        // Act
        employeeService.updateEmployee(employee);

        // Assert
        verify(employeeRepository, times(1)).updateEmployee(employee);
    }

    @Test
    void deleteEmployee_ShouldCallRepositoryDeleteEmployee() {
        // Act
        employeeService.deleteEmployee(employee);

        // Assert
        verify(employeeRepository, times(1)).deleteEmployee(employee);
    }

    @Test
    void getAllEmployees_ShouldReturnListOfEmployees() {
        // Arrange
        Employee employee2 = Employee.builder()
                .firstName("Roman")
                .lastName("Jack")
                .email("rjack@gmail.com")
                .build();
        List<Employee> employees = Arrays.asList(employee, employee2);
        when(employeeRepository.getAllEmployees()).thenReturn(employees);

        // Act
        List<Employee> result = employeeService.getAllEmployees();

        // Assert
        assertEquals(employees, result);
    }
}

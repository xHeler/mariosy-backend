package com.deloitte.ads.repositories;

import com.deloitte.ads.models.Employee;
import com.deloitte.ads.repositories.impl.LocalEmployeeRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class LocalEmployeeRepositoryImplTestDto {
    @Mock
    private Employee employee;

    private LocalEmployeeRepositoryImpl employeeRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        employeeRepository = new LocalEmployeeRepositoryImpl();
    }

    @Test
    void should_AddEmployeeToRepository_When_SaveEmployee() {
        // Act
        employeeRepository.saveEmployee(employee);

        // Assert
        assertTrue(employeeRepository.getEmployeeMap().containsValue(employee));
    }

    @Test
    void should_ReturnEmployee_When_GetEmployeeById_WithExistingEmployee() {
        // Arrange
        employee = Employee.builder().build();
        UUID id = employee.getId();
        employeeRepository.saveEmployee(employee);

        // Act
        Optional<Employee> result = employeeRepository.getEmployeeById(id);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(employee, result.get());
    }

    @Test
    void should_ReturnEmptyOptional_When_GetEmployeeById_WithNonExistingEmployee() {
        // Arrange
        UUID id = UUID.randomUUID();

        // Act
        Optional<Employee> result = employeeRepository.getEmployeeById(id);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void should_ReturnListOfEmployees_When_FindAllEmployeesByFirstName_WithExistingEmployees() {
        // Arrange
        String firstName = "John";
        Employee employee1 = Employee
                .builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build();
        Employee employee2 = Employee
                .builder()
                .firstName("John")
                .lastName("Smith")
                .email("john.smith@example.com")
                .build();
        Employee employee3 = Employee
                .builder()
                .firstName("Jane")
                .lastName("Doe")
                .email("jane.doe@example.com")
                .build();
        employeeRepository.saveEmployee(employee1);
        employeeRepository.saveEmployee(employee2);
        employeeRepository.saveEmployee(employee3);

        // Act
        List<Employee> result = employeeRepository.findAllEmployeesByFirstName(firstName);

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(employee1));
        assertTrue(result.contains(employee2));
    }

    @Test
    void should_ReturnEmptyList_When_FindAllEmployeesByFirstName_WithNoEmployees() {
        // Arrange
        String firstName = "John";

        // Act
        List<Employee> result = employeeRepository.findAllEmployeesByFirstName(firstName);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void should_UpdateEmployeeInRepository_When_UpdateEmployee() {
        // Arrange
        employee = Employee.builder().build();
        UUID id = employee.getId();
        employeeRepository.saveEmployee(employee);

        // Act
        Employee updatedEmployee = Employee
                .builder()
                .id(id)
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build();
        employeeRepository.updateEmployee(updatedEmployee);

        // Assert
        assertEquals(updatedEmployee, employeeRepository.getEmployeeMap().get(id));
    }

    @Test
    void should_RemoveEmployeeFromRepository_When_DeleteEmployee() {
        // Arrange
        UUID id = UUID.randomUUID();
        employee.setId(id);
        employeeRepository.saveEmployee(employee);

        // Act
        employeeRepository.deleteEmployee(employee);

        // Assert
        assertFalse(employeeRepository.getEmployeeMap().containsKey(id));
    }

    @Test
    void should_ReturnListOfEmployees_When_GetAllEmployees_WithExistingEmployees() {
        // Arrange
        Employee employee1 = Employee
                .builder()
                .firstName("John")
                .lastName("Doe")
                .email("john.doe@example.com")
                .build();
        Employee employee2 = Employee
                .builder()
                .firstName("Jane")
                .lastName("Smith")
                .email("jane.smith@example.com")
                .build();
        employeeRepository.saveEmployee(employee1);
        employeeRepository.saveEmployee(employee2);

        // Act
        List<Employee> result = employeeRepository.getAllEmployees();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(employee1));
        assertTrue(result.contains(employee2));
    }

    @Test
    void should_ReturnEmptyList_When_GetAllEmployees_WithNoEmployees() {
        // Act
        List<Employee> result = employeeRepository.getAllEmployees();

        // Assert
        assertTrue(result.isEmpty());
    }
}

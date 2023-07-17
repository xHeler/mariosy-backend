package com.deloitte.ads.services;

import com.deloitte.ads.dto.EmployeeDto;
import com.deloitte.ads.exceptions.EmployeeAlreadyExistException;
import com.deloitte.ads.factories.EmployeeFactory;
import com.deloitte.ads.models.Employee;
import com.deloitte.ads.repositories.EmployeeRepository;
import com.deloitte.ads.utils.DtoConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmployeeCreationService {

    private final EmployeeRepository employeeRepository;

    public ResponseEntity<EmployeeDto> saveEmployee(EmployeeDto employeeDto) {
        log.info("Saving employee: {}", employeeDto);
        if (employeeRepository.isEmployeeWithEmailExist(employeeDto.getEmail()))
            throw new EmployeeAlreadyExistException("Employee with this email already exist!");
        Employee employee = EmployeeFactory.createEmployee(employeeDto);
        employeeRepository.saveEmployee(employee);
        return ResponseEntity.ok(employeeDto);
    }

    public ResponseEntity<EmployeeDto> saveEmployee(String id, EmployeeDto employeeDto) {
        log.info("Saving employee with ID: {}, data: {}", id, employeeDto);
        Employee employee = EmployeeFactory.createEmployee(id, employeeDto);
        employeeRepository.saveEmployee(employee);
        return ResponseEntity.ok(DtoConverter.convertToDto(employee));
    }

}

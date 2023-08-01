package com.deloitte.ads.services;

import com.deloitte.ads.dto.MariosDto;
import com.deloitte.ads.exceptions.EmployeeNotFoundException;
import com.deloitte.ads.exceptions.SelfMariosException;
import com.deloitte.ads.factories.MariosFactory;
import com.deloitte.ads.models.Employee;
import com.deloitte.ads.models.Marios;
import com.deloitte.ads.models.ReactionType;
import com.deloitte.ads.repositories.MariosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MariosCreationService {

    private final EmployeeRetrievalService employeeRetrievalService;
    private final MariosRepository mariosRepository;

    public void addMariosFromDto(MariosDto mariosDto) {
        Employee sender = employeeRetrievalService.getEmployeeById(UUID.fromString(mariosDto.getSenderId()));
        List<Employee> receivers = employeeRetrievalService.getAllEmployeesByIds(mariosDto.getReceiversId());
        addMarios(sender, receivers, mariosDto.getMessage(), mariosDto.getTitle(), mariosDto.getReaction());
    }

    public void addMarios(Employee sender, Employee receiver, String message, String title, ReactionType reaction) {
        validateEmployeesExistence(sender, receiver);
        validateNotSelfMarios(sender, receiver);

        Marios marios = MariosFactory.createMarios(sender, receiver, message, title, reaction);
        saveMarios(marios);
    }

    public void addMarios(Employee sender, List<Employee> receivers, String message, String title, ReactionType reaction) {
        receivers.forEach(employee -> {
            validateEmployeesExistence(sender, employee);
            validateNotSelfMarios(sender, employee);

            Marios marios = MariosFactory.createMarios(sender, employee, message, title, reaction);
            saveMarios(marios);
        });
    }

    private void validateEmployeesExistence(Employee sender, Employee receiver) {
        if (!employeeRetrievalService.isEmployeeExist(sender)) {
            throw new EmployeeNotFoundException("Employee " + sender + " does not exist!");
        }
        if (!employeeRetrievalService.isEmployeeExist(receiver)) {
            throw new EmployeeNotFoundException("Employee " + receiver + " does not exist!");
        }
    }

    private void validateNotSelfMarios(Employee sender, Employee receiver) {
        if (sender.getId().equals(receiver.getId())) {
            throw new SelfMariosException("You cannot give Marios to yourself!");
        }
    }

    private void saveMarios(Marios marios) {
        mariosRepository.saveMarios(marios);
    }
}

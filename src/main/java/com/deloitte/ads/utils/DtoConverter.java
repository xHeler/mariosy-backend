package com.deloitte.ads.utils;

import com.deloitte.ads.dto.EmployeeDto;
import com.deloitte.ads.dto.MariosDto;
import com.deloitte.ads.factories.EmployeeFactory;
import com.deloitte.ads.models.Employee;
import com.deloitte.ads.models.Marios;
import com.deloitte.ads.models.ReactionType;

import java.util.List;

public class DtoConverter {
    public static Employee convertToEntity(EmployeeDto employeeDto) {
        return EmployeeFactory.createEmployee(employeeDto);
    }

    public static EmployeeDto convertToDto(Employee employee) {
        return EmployeeDto
                .builder()
                .firstName(employee.getFirstName())
                .lastName(employee.getLastName())
                .email(employee.getEmail())
                .build();
    }

    public static MariosDto convertToDto(Marios mario) {
        return MariosDto.builder()
                .title(mario.getTitle())
                .senderId(mario.getSender().getId().toString())
                .receiversId(List.of(mario.getReceiver().getId().toString()))
                .message(mario.getMessage())
                .reaction(ReactionType.valueOf(mario.getReaction()))
                .build();
    }
}

package com.deloitte.ads.dto;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class EmployeeDto {

    private String firstName;

    private String lastName;

    private String email;

}

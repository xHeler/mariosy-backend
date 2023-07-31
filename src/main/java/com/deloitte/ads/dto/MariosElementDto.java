package com.deloitte.ads.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MariosElementDto {
    private MariosDto marios;
    private EmployeeDto sender;
    private EmployeeDto receiver;
}

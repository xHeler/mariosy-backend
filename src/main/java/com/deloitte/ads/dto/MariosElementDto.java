package com.deloitte.ads.dto;

import com.deloitte.ads.models.ReactionType;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MariosElementDto {
    private String senderId;
    private List<String> receiversId;
    private String message;
    private ReactionType reaction;
    private EmployeeDto sender;
    private EmployeeDto receiver;
}

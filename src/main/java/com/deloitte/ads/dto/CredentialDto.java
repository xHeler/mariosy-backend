package com.deloitte.ads.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CredentialDto {
    private String value;
    private Boolean temporary = false;
}

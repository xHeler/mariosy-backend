package com.deloitte.ads.models;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class Employee {

    @Builder.Default
    private UUID id = UUID.randomUUID();

    private String firstName;

    private String lastName;

    private String email;

}

package com.deloitte.ads.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Kudos {

    private Long id;

    private Employee sender;

    private Employee receiver;

    private String message;
}

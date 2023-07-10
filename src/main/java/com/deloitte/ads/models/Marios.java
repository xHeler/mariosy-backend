package com.deloitte.ads.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Marios {

    private Long id;

    private Employee sender;

    private Employee receiver;

    private String message;

    private ReactionType reaction;
}

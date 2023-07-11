package com.deloitte.ads.models;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class Marios {

    @Builder.Default
    private UUID id = UUID.randomUUID();

    private Employee sender;

    private Employee receiver;

    private String message;

    private ReactionType reaction;
}

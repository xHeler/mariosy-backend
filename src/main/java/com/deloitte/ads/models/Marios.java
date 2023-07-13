package com.deloitte.ads.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "marioses")
public class Marios {

    @Id
    @Builder.Default
    private UUID id = UUID.randomUUID();

    private Employee sender;

    private Employee receiver;

    private String message;

    private ReactionType reaction; //todo: change to string
}

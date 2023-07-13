package com.deloitte.ads.factories;

import com.deloitte.ads.models.Employee;
import com.deloitte.ads.models.Marios;
import com.deloitte.ads.models.ReactionType;

import java.util.UUID;

public class MariosFactory {

    public static Marios createMarios(Employee sender, Employee receiver, String message, ReactionType reaction) {
        return Marios.builder()
                .id(UUID.randomUUID())
                .sender(sender)
                .receiver(receiver)
                .message(message)
                .reaction(reaction)
                .build();
    }

    public static Marios createMarios() {
        return Marios.builder().build();
    }
}

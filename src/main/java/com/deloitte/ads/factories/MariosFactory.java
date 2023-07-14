package com.deloitte.ads.factories;

import com.deloitte.ads.models.Employee;
import com.deloitte.ads.models.Marios;
import com.deloitte.ads.models.enums.ReactionType;

public class MariosFactory {

    public static Marios createMarios(Employee sender, Employee receiver, String message, ReactionType reaction) {
        return Marios.builder()
                .message(message)
                .reaction(reaction.name())
                .sender(sender)
                .receiver(receiver)
                .build();
    }


}

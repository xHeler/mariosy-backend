package com.deloitte.ads.factories;

import com.deloitte.ads.models.Employee;
import com.deloitte.ads.models.Marios;
import com.deloitte.ads.models.ReactionType;

public final class MariosFactory {

    private MariosFactory() {

    }

    public static Marios createMarios(Employee sender, Employee receiver, String message, String title, ReactionType reaction) {
        return Marios.builder()
                .title(title)
                .message(message)
                .reaction(reaction.name())
                .sender(sender)
                .receiver(receiver)
                .build();
    }


}

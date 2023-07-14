package com.deloitte.ads.factories;

import com.deloitte.ads.dto.MariosDto;
import com.deloitte.ads.models.Employee;
import com.deloitte.ads.models.Marios;
import com.deloitte.ads.models.ReactionType;

public class MariosFactory {

    public static Marios createMarios(Employee sender, Employee receiver, String message, ReactionType reaction) {
        return Marios.builder()
                .message(message)
                .reaction(reaction)
                .sender(sender)
                .receiver(receiver)
                .build();
    }

    public static Marios createMariosWithDto(Employee sender, Employee receiver, MariosDto mariosDto) {
        String message = mariosDto.getMessage();
        ReactionType reaction = mariosDto.getReaction();
        return createMarios(sender, receiver, message, reaction);
    }

}

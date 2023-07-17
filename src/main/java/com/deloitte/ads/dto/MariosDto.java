package com.deloitte.ads.dto;

import com.deloitte.ads.models.ReactionType;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MariosDto {
    private String senderId;
    private List<String> receiversId;
    private String message;
    private ReactionType reaction;
}

package com.deloitte.ads.dto;

import com.deloitte.ads.models.Marios;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class MariosListDto {
    private List<MariosElementDto> mariosElementList;
    private Integer mariosSize;
}

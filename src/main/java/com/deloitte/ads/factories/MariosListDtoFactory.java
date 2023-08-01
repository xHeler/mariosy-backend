package com.deloitte.ads.factories;

import com.deloitte.ads.dto.MariosElementDto;
import com.deloitte.ads.dto.MariosListDto;
import com.deloitte.ads.models.Marios;

import java.util.List;

public class MariosListDtoFactory {

    public MariosListDtoFactory() {
    }

    public static MariosListDto createMariosListDto(List<MariosElementDto> mariosList) {
        return MariosListDto
                .builder()
                .mariosElementList(mariosList)
                .mariosSize(mariosList.size())
                .build();
    }

}

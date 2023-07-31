package com.deloitte.ads.factories;

import com.deloitte.ads.dto.MariosListDto;
import com.deloitte.ads.models.Marios;

import java.util.List;

public class MariosListDtoFactory {

    public MariosListDtoFactory() {
    }

    public static MariosListDto createMariosListDto(List<Marios> mariosList) {
        return MariosListDto
                .builder()
                .mariosList(mariosList)
                .mariosSize(mariosList.size())
                .build();
    }

}

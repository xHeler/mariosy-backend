package com.deloitte.ads.services;

import com.deloitte.ads.models.Marios;
import com.deloitte.ads.repositories.MariosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MariosManagementService {

    private final MariosRepository mariosRepository;

    public void updateMarios(Marios marios) {
        mariosRepository.updateMarios(marios);
    }

    public void deleteMarios(Marios marios) {
        mariosRepository.deleteMarios(marios);
    }
}
package com.deloitte.ads.repositories.interfaces;

import com.deloitte.ads.models.Marios;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MariosRepository {
    void saveMarios(Marios marios);

    Optional<Marios> getMariosById(UUID id);

    void updateMarios(Marios marios);

    void deleteMarios(Marios marios);

    List<Marios> getAllMarios();
}

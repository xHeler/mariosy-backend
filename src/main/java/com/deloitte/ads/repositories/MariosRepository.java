package com.deloitte.ads.repositories;

import com.deloitte.ads.models.Marios;

import java.util.List;
import java.util.Optional;

public interface MariosRepository {
    void saveMarios(Marios marios);
    Optional<Marios> getMariosById(Long id);
    void updateMarios(Marios marios);
    void deleteMarios(Marios marios);

    List<Marios> getAllMarios();
}

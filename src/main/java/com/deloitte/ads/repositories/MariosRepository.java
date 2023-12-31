package com.deloitte.ads.repositories;

import com.deloitte.ads.models.Marios;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MariosRepository {
    void saveMarios(Marios marios);

    Optional<Marios> getMariosById(UUID id);

    void updateMarios(Marios marios);

    void deleteMarios(Marios marios);

    List<Marios> getAllMarios();
}

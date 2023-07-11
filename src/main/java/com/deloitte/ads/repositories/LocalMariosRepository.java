package com.deloitte.ads.repositories;

import com.deloitte.ads.models.Marios;
import com.deloitte.ads.repositories.interfaces.MariosRepository;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.*;

@Data
@NoArgsConstructor
@Repository
public class LocalMariosRepository implements MariosRepository {
    private Map<UUID, Marios> mariosMap = new HashMap<>();

    @Override
    public void saveMarios(Marios marios) {
        mariosMap.put(marios.getId(), marios);
    }

    @Override
    public Optional<Marios> getMariosById(UUID id) {
        return Optional.ofNullable(mariosMap.get(id));
    }

    @Override
    public void updateMarios(Marios marios) {
        mariosMap.put(marios.getId(), marios);
    }

    @Override
    public void deleteMarios(Marios marios) {
        mariosMap.remove(marios.getId());
    }

    @Override
    public List<Marios> getAllMarios() {
        return new ArrayList<>(mariosMap.values());
    }
}

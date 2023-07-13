package com.deloitte.ads.repositories;

import com.deloitte.ads.models.Marios;
import com.deloitte.ads.repositories.interfaces.MariosRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class MongoMariosRepository implements MariosRepository {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public MongoMariosRepository(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void saveMarios(Marios marios) {
        mongoTemplate.save(marios);
    }

    @Override
    public Optional<Marios> getMariosById(UUID id) {
        return Optional.ofNullable(mongoTemplate.findById(id, Marios.class));
    }

    @Override
    public void updateMarios(Marios marios) {
        mongoTemplate.save(marios);
    }

    @Override
    public void deleteMarios(Marios marios) {
        mongoTemplate.remove(marios);
    }

    @Override
    public List<Marios> getAllMarios() {
        return mongoTemplate.findAll(Marios.class);
    }
}
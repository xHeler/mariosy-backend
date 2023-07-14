package com.deloitte.ads.services;

import com.deloitte.ads.dto.MariosDto;
import com.deloitte.ads.models.Marios;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class MariosService {
    private final MariosCreationService creationService;
    private final MariosRetrievalService retrievalService;
    private final MariosManagementService managementService;

    public ResponseEntity<Void> addMariosFromDto(MariosDto mariosDto) {
        log.info("Adding new Marios from DTO: {}", mariosDto);
        creationService.addMariosFromDto(mariosDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public Marios getMariosById(UUID id) {
        log.info("Fetching Marios by ID: {}", id);
        return retrievalService.getMariosById(id);
    }

    public void updateMarios(Marios marios) {
        log.info("Updating Marios: {}", marios);
        managementService.updateMarios(marios);
    }

    public ResponseEntity<Void> updateMariosById(String mariosId) {
        log.info("Updating Marios by ID: {}", mariosId);
        Marios marios = getMariosById(UUID.fromString(mariosId));
        updateMarios(marios);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public void deleteMarios(Marios marios) {
        log.info("Deleting Marios: {}", marios);
        managementService.deleteMarios(marios);
    }

    public ResponseEntity<Void> removeMariosById(String mariosId) {
        log.info("Removing Marios by ID: {}", mariosId);
        Marios marios = getMariosById(UUID.fromString(mariosId));
        deleteMarios(marios);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<List<Marios>> getAllMarios() {
        log.info("Fetching all Marios");
        return ResponseEntity.ok(retrievalService.getAllMarios());
    }

    public ResponseEntity<List<Marios>> getAllSentMariosByEmployeeId(String id) {
        log.info("Fetching all sent Marios for employee ID: {}", id);
        return ResponseEntity.ok(retrievalService.getAllSentMariosByEmployeeId(id));
    }

    public ResponseEntity<List<Marios>> getAllReceiveMariosByEmployeeId(String id) {
        log.info("Fetching all received Marios for employee ID: {}", id);
        return ResponseEntity.ok(retrievalService.getAllReceiveMariosByEmployeeId(id));
    }
}

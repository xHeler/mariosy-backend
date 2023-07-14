package com.deloitte.ads.services;

import com.deloitte.ads.dto.MariosDto;
import com.deloitte.ads.models.Marios;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MariosService {
    private final MariosCreationService creationService;
    private final MariosRetrievalService retrievalService;
    private final MariosManagementService managementService;

    public ResponseEntity<?> addMariosFromDto(MariosDto mariosDto) {
        creationService.addMariosFromDto(mariosDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public Marios getMariosById(UUID id) {
        return retrievalService.getMariosById(id);
    }

    public void updateMarios(Marios marios) {
        managementService.updateMarios(marios);
    }

    public ResponseEntity<?> updateMariosById(String mariosId) {
        Marios marios = getMariosById(UUID.fromString(mariosId));
        updateMarios(marios);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public void deleteMarios(Marios marios) {
        managementService.deleteMarios(marios);
    }

    public ResponseEntity<?> removeMariosById(String mariosId) {
        Marios marios = getMariosById(UUID.fromString(mariosId));
        deleteMarios(marios);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<List<Marios>> getAllMarios() {
        return ResponseEntity.ok(retrievalService.getAllMarios());
    }

    public ResponseEntity<List<Marios>> getAllSentMariosByEmployeeId(String id) {
        return ResponseEntity.ok(retrievalService.getAllSentMariosByEmployeeId(id));
    }

    public ResponseEntity<List<Marios>> getAllReceiveMariosByEmployeeId(String id) {
        return ResponseEntity.ok(retrievalService.getAllReceiveMariosByEmployeeId(id));
    }
}

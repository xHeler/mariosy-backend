package com.deloitte.ads.services;

import com.deloitte.ads.exceptions.MariosNotFoundException;
import com.deloitte.ads.models.Marios;
import com.deloitte.ads.repositories.MariosRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MariosRetrievalService {

    private final MariosRepository mariosRepository;

    public List<Marios> getAllMarios() {
        return mariosRepository.getAllMarios();
    }

    public Marios getMariosById(UUID id) {
        return mariosRepository.getMariosById(id)
                .orElseThrow(() -> new MariosNotFoundException("Marios with id=" + id + " does not exist!"));
    }

    public List<Marios> getAllSentMariosByEmployeeId(String id) {
        UUID employeeId = UUID.fromString(id);
        return mariosRepository.getAllMarios()
                .stream()
                .filter(marios -> marios.getSender().getId().equals(employeeId))
                .collect(Collectors.toList());
    }

    public List<Marios> getAllReceiveMariosByEmployeeId(String id) {
        UUID employeeId = UUID.fromString(id);
        return mariosRepository.getAllMarios()
                .stream()
                .filter(marios -> marios.getReceiver().getId().equals(employeeId))
                .collect(Collectors.toList());
    }
}

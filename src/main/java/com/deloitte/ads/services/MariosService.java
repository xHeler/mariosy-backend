package com.deloitte.ads.services;

import com.deloitte.ads.dto.MariosDto;
import com.deloitte.ads.dto.MariosElementDto;
import com.deloitte.ads.dto.MariosListDto;
import com.deloitte.ads.factories.MariosListDtoFactory;
import com.deloitte.ads.models.Employee;
import com.deloitte.ads.models.Marios;
import com.deloitte.ads.models.ReactionType;
import com.deloitte.ads.utils.DtoConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class MariosService {
    private final MariosCreationService creationService;
    private final MariosRetrievalService retrievalService;
    private final MariosManagementService managementService;
    private final EmployeeRetrievalService employeeRetrievalService;

    public ResponseEntity<Void> addMariosFromDto(MariosDto mariosDto) {
        log.info("Adding new Marios from DTO: {}", mariosDto);
        creationService.addMariosFromDto(mariosDto);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<Marios> getMariosById(String id) {
        log.info("Fetching Marios by ID: {}", id);
        UUID uuid = UUID.fromString(id);
        Marios marios = retrievalService.getMariosById(uuid);
        return ResponseEntity.ok(marios);
    }

    public void updateMarios(Marios marios) {
        log.info("Updating Marios: {}", marios);
        managementService.updateMarios(marios);
    }

    public ResponseEntity<Void> updateMariosById(String mariosId) {
        log.info("Updating Marios by ID: {}", mariosId);
        UUID uuid = UUID.fromString(mariosId);
        Marios marios = retrievalService.getMariosById(uuid);
        updateMarios(marios);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public void deleteMarios(Marios marios) {
        log.info("Deleting Marios: {}", marios);
        managementService.deleteMarios(marios);
    }

    public ResponseEntity<Void> removeMariosById(String mariosId) {
        log.info("Removing Marios by ID: {}", mariosId);
        UUID uuid = UUID.fromString(mariosId);
        Marios marios = retrievalService.getMariosById(uuid);
        deleteMarios(marios);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<List<MariosElementDto>> getAllMarios() {
        log.info("Fetching all Marios");
        List<Marios> allMarios = retrievalService.getAllMarios();
        List<MariosElementDto> marioses = mapMariosToDto(allMarios);
        return ResponseEntity.ok(marioses);
    }

    public ResponseEntity<List<MariosElementDto>> getAllSentMariosByEmployeeId(String id) {
        log.info("Fetching all sent Marios for employee ID: {}", id);
        List<Marios> allSentMariosByEmployeeId = retrievalService.getAllSentMariosByEmployeeId(id);
        List<MariosElementDto> sentMarios = mapMariosToDto(allSentMariosByEmployeeId);
        return ResponseEntity.ok(sentMarios);
    }

    public ResponseEntity<List<MariosElementDto>> getAllReceiveMariosByEmployeeId(String id) {
        log.info("Fetching all received Marios for employee ID: {}", id);
        List<Marios> allReceivedMariosByEmployeeId = retrievalService.getAllReceiveMariosByEmployeeId(id);
        List<MariosElementDto> receivedMarios = mapMariosToDto(allReceivedMariosByEmployeeId);
        return ResponseEntity.ok(receivedMarios);
    }

    private List<MariosElementDto> mapMariosToDto(List<Marios> allMarios) {
        List<String> senderIds = extractIdsFromMarios(allMarios, Marios::getSender);
        List<String> receiverIds = extractIdsFromMarios(allMarios, Marios::getReceiver);
        Map<String, Employee> sendersEmployeeMap = fetchEmployees(senderIds);
        Map<String, Employee> receiversEmployeeMap = fetchEmployees(receiverIds);
        List<MariosElementDto> marioses = new ArrayList<>();

        for (Marios mario : allMarios) {
            MariosElementDto dto = mapMariosToDto(mario, sendersEmployeeMap, receiversEmployeeMap);
            marioses.add(dto);
        }

        return marioses;
    }

    private List<String> extractIdsFromMarios(List<Marios> mariosList, Function<Marios, Employee> employeeExtractor) {
        return mariosList.stream()
                .map(mario -> employeeExtractor.apply(mario).getId().toString())
                .collect(Collectors.toList());
    }

    private Map<String, Employee> fetchEmployees(List<String> employeeIds) {
        List<Employee> employees = employeeRetrievalService.getAllEmployeesByIds(employeeIds);
        return employees.stream()
                .collect(Collectors.toMap(emp -> emp.getId().toString(), Function.identity()));
    }

    private MariosElementDto mapMariosToDto(Marios mario, Map<String, Employee> sendersMap, Map<String, Employee> receiversMap) {
        MariosElementDto dto = MariosElementDto.builder()
                .senderId(mario.getSender().getId().toString())
                .receiversId(Collections.singletonList(mario.getReceiver().getId().toString()))
                .message(mario.getMessage())
                .reaction(ReactionType.valueOf(mario.getReaction()))
                .build();

        Employee sender = sendersMap.get(dto.getSenderId());
        if (sender != null) {
            dto.setSender(DtoConverter.convertToDto(sender));
        }

        Employee receiver = receiversMap.get(dto.getReceiversId().get(0));
        if (receiver != null) {
            dto.setReceiver(DtoConverter.convertToDto(receiver));
        }

        return dto;
    }
}

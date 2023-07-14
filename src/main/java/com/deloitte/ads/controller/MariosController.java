package com.deloitte.ads.controller;

import com.deloitte.ads.dto.MariosDto;
import com.deloitte.ads.models.Marios;
import com.deloitte.ads.services.MariosService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/marios")
@RequiredArgsConstructor
@Slf4j
public class MariosController {
    private final MariosService mariosService;

    @GetMapping
    ResponseEntity<List<Marios>> getAllMarios() {
        log.info("Fetching all Marios");
        return mariosService.getAllMarios();
    }

    @GetMapping("/sent/{employeeId}")
    ResponseEntity<List<Marios>> getAllSentMariosByEmployeeId(@PathVariable String employeeId) {
        log.info("Fetching all sent Marios for employee ID: {}", employeeId);
        return mariosService.getAllSentMariosByEmployeeId(employeeId);
    }

    @GetMapping("/receive/{employeeId}")
    ResponseEntity<List<Marios>> getAllReceiveMariosByEmployeeId(@PathVariable String employeeId) {
        log.info("Fetching all received Marios for employee ID: {}", employeeId);
        return mariosService.getAllReceiveMariosByEmployeeId(employeeId);
    }

    @PostMapping
    ResponseEntity<?> addMarios(@RequestBody MariosDto mariosDto) {
        log.info("Adding new Marios: {}", mariosDto);
        return mariosService.addMariosFromDto(mariosDto);
    }

    @DeleteMapping("/{mariosId}")
    ResponseEntity<?> removeMariosByMariosId(@PathVariable String mariosId) {
        log.info("Removing Marios with ID: {}", mariosId);
        return mariosService.removeMariosById(mariosId);
    }

    @PutMapping("/{mariosId}")
    ResponseEntity<?> updateMarios(@PathVariable String mariosId) {
        log.info("Updating Marios with ID: {}", mariosId);
        return mariosService.updateMariosById(mariosId);
    }
}

package com.deloitte.ads.controller;

import com.deloitte.ads.dto.MariosDto;
import com.deloitte.ads.models.Marios;
import com.deloitte.ads.services.MariosService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/marios")
@RequiredArgsConstructor
public class MariosController {
    private final MariosService mariosService;

    @GetMapping
    ResponseEntity<List<Marios>> getAllMarios() {
        return mariosService.getAllMarios();
    }

    @GetMapping("/sent/{employeeId}")
    ResponseEntity<List<Marios>> getAllSentMariosByEmployeeId(@PathVariable String employeeId) {
        return mariosService.getAllSentMariosByEmployeeId(employeeId);
    }

    @GetMapping("/receive/{employeeId}")
    ResponseEntity<List<Marios>> getAllReceiveMariosByEmployeeId(@PathVariable String employeeId) {
        return mariosService.getAllReceiveMariosByEmployeeId(employeeId);
    }

    @PostMapping
    ResponseEntity<?> addMarios(@RequestBody MariosDto mariosDto) {
        return mariosService.addMariosFromDto(mariosDto);
    }

    @DeleteMapping("/{mariosId}")
    ResponseEntity<?> removeMariosByMariosId(@PathVariable String mariosId) {
        return mariosService.removeMariosById(mariosId);
    }

    @PutMapping("/{mariosId}")
    ResponseEntity<?> updateMarios(@PathVariable String mariosId) {
        return mariosService.updateMariosById(mariosId);
    }

}

package com.deloitte.ads.controller;

import com.deloitte.ads.dto.MariosDto;
import com.deloitte.ads.models.Employee;
import com.deloitte.ads.models.Marios;
import com.deloitte.ads.services.EmployeeService;
import com.deloitte.ads.services.MariosService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/marios") // todo: remove v1/
@RequiredArgsConstructor
public class MariosController {
    private final MariosService mariosService;
    private final EmployeeService employeeService;

    @GetMapping
    ResponseEntity<List<Marios>> getAllMarios() {
        return new ResponseEntity<>(mariosService.getAllMarios(), HttpStatus.OK);
    }

    @GetMapping("/sent/{id}")
    ResponseEntity<List<Marios>> getAllSentMariosByEmployeeId(@PathVariable String id) { //todo: id -> EmployeeId
        return new ResponseEntity<>(mariosService.getAllSentMariosByEmployeeId(id), HttpStatus.OK);
    }

    @GetMapping("/receive/{id}")
    ResponseEntity<List<Marios>> getAllReceiveMariosByEmployeeId(@PathVariable String id) {
        return new ResponseEntity<>(mariosService.getAllReceiveMariosByEmployeeId(id), HttpStatus.OK);
    }

    @PostMapping
    ResponseEntity<Void> addMarios(@RequestBody MariosDto mariosDto) { //todo: pass business logic into service
        try {
            Employee sender = employeeService.getEmployeeById(UUID.fromString(mariosDto.getSenderId()));
            List<Employee> receivers = employeeService.getAllEmployeesByIds(mariosDto.getReceiversId());
            mariosService.addMarios(sender, receivers, mariosDto.getMessage(), mariosDto.getReaction());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> removeMarios(@PathVariable String id) {
        try {
            Marios marios = mariosService.getMariosById(UUID.fromString(id));
            mariosService.deleteMarios(marios);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{id}")
    ResponseEntity<Void> updateMarios(@PathVariable String id) {
        try {
            Marios marios = mariosService.getMariosById(UUID.fromString(id));
            mariosService.updateMarios(marios);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (Exception exception) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

}

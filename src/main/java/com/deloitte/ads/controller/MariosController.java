package com.deloitte.ads.controller;

import com.deloitte.ads.dto.MariosDto;
import com.deloitte.ads.dto.MariosElementDto;
import com.deloitte.ads.dto.MariosListDto;
import com.deloitte.ads.models.Marios;
import com.deloitte.ads.services.MariosService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/marios")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://bkwiatkowski-frontend.s3-website.eu-central-1.amazonaws.com")
@Slf4j
public class MariosController {
    private final MariosService mariosService;

    @GetMapping
    ResponseEntity<MariosListDto> getAllMarios(@RequestParam(defaultValue = "0") int page,
                                               @RequestParam(defaultValue = "12") int size) {
        log.info("Fetching all Marios with pagination - Page: {}, Size: {}", page, size);
        return mariosService.getAllMarios(page, size);
    }

    @GetMapping("/{mariosId}")
    ResponseEntity<Marios> getMariosById(@PathVariable("mariosId") String mariosId) {
        log.info("Get marios by ID: {}", mariosId);
        return mariosService.getMariosById(mariosId);
    }

    @GetMapping("/sent/{employeeId}")
    ResponseEntity<MariosListDto> getAllSentMariosByEmployeeId(@RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "12") int size,
                                                               @PathVariable String employeeId) {
        log.info("Fetching all sent Marios for employee ID: {} with pagination - Page: {}, Size: {}", employeeId, page, size);
        return mariosService.getAllSentMariosByEmployeeId(employeeId, page, size);
    }

    @GetMapping("/receive/{employeeId}")
    ResponseEntity<MariosListDto> getAllReceiveMariosByEmployeeId(@RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "12") int size,
                                                                  @PathVariable String employeeId) {
        log.info("Fetching all received Marios for employee ID: {} with pagination - Page: {}, Size: {}", employeeId, page, size);
        return mariosService.getAllReceiveMariosByEmployeeId(employeeId, page, size);
    }

    @PostMapping
    ResponseEntity<Void> addMarios(@RequestBody MariosDto mariosDto) {
        log.info("Adding new Marios: {}", mariosDto);
        return mariosService.addMariosFromDto(mariosDto);
    }

    @DeleteMapping("/{mariosId}")
    ResponseEntity<Void> removeMariosByMariosId(@PathVariable String mariosId) {
        log.info("Removing Marios with ID: {}", mariosId);
        return mariosService.removeMariosById(mariosId);
    }

    @PutMapping("/{mariosId}")
    ResponseEntity<Void> updateMarios(@PathVariable String mariosId) {
        log.info("Updating Marios with ID: {}", mariosId);
        return mariosService.updateMariosById(mariosId);
    }
}

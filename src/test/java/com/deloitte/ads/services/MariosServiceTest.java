package com.deloitte.ads.services;

import com.deloitte.ads.dto.MariosDto;
import com.deloitte.ads.factories.EmployeeFactory;
import com.deloitte.ads.models.Employee;
import com.deloitte.ads.models.Marios;
import com.deloitte.ads.models.enums.ReactionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MariosServiceTest {

    @Mock
    private MariosCreationService creationService;

    @Mock
    private MariosRetrievalService retrievalService;

    @Mock
    private MariosManagementService managementService;

    @InjectMocks
    private MariosService mariosService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addMariosFromDto_ShouldReturnOkResponse() {
        // Given
        Employee sender = EmployeeFactory.createEmployee("John", "Doe");
        Employee receiver = EmployeeFactory.createEmployee("Alan", "Tate");
        ReactionType reaction = ReactionType.GOOD_JOB;
        String senderId = sender.getId().toString();
        String receiverId = receiver.getId().toString();

        MariosDto mariosDto = MariosDto.builder().senderId(senderId)
                .receiversId(List.of(receiverId)).message("message 1").reaction(reaction).build();

        // When
        ResponseEntity<?> response = mariosService.addMariosFromDto(mariosDto);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(creationService).addMariosFromDto(mariosDto);
    }

    @Test
    void getMariosById_ShouldCallRetrievalService() {
        // Given
        UUID mariosId = UUID.randomUUID();
        Marios expectedMarios = new Marios(/* initialize with necessary values */);
        when(retrievalService.getMariosById(mariosId)).thenReturn(expectedMarios);

        // When
        Marios actualMarios = mariosService.getMariosById(mariosId);

        // Then
        assertEquals(expectedMarios, actualMarios);
        verify(retrievalService).getMariosById(mariosId);
    }

    @Test
    void updateMarios_ShouldCallManagementService() {
        // Given
        Marios marios = new Marios(/* initialize with necessary values */);

        // When
        mariosService.updateMarios(marios);

        // Then
        verify(managementService).updateMarios(marios);
    }

    @Test
    void updateMariosById_ShouldCallRetrievalAndManagementServices() {
        // Given
        UUID mariosUUID = UUID.randomUUID();
        Marios expectedMarios = new Marios(/* initialize with necessary values */);
        when(retrievalService.getMariosById(mariosUUID)).thenReturn(expectedMarios);

        // When
        ResponseEntity<?> response = mariosService.updateMariosById(mariosUUID.toString());

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(retrievalService).getMariosById(mariosUUID);
        verify(managementService).updateMarios(expectedMarios);
    }

    @Test
    void deleteMarios_ShouldCallManagementService() {
        // Given
        Marios marios = new Marios(/* initialize with necessary values */);

        // When
        mariosService.deleteMarios(marios);

        // Then
        verify(managementService).deleteMarios(marios);
    }

    @Test
    void removeMariosById_ShouldCallRetrievalAndManagementServices() {
        // Given
        UUID mariosUUID = UUID.randomUUID();
        Marios expectedMarios = new Marios(/* initialize with necessary values */);
        when(retrievalService.getMariosById(mariosUUID)).thenReturn(expectedMarios);

        // When
        ResponseEntity<?> response = mariosService.removeMariosById(mariosUUID.toString());

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(retrievalService).getMariosById(mariosUUID);
        verify(managementService).deleteMarios(expectedMarios);
    }

    @Test
    void getAllMarios_ShouldReturnListOfMarios() {
        // Given
        List<Marios> expectedMariosList = List.of(new Marios(/* initialize with necessary values */));
        when(retrievalService.getAllMarios()).thenReturn(expectedMariosList);

        // When
        ResponseEntity<List<Marios>> response = mariosService.getAllMarios();

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedMariosList, response.getBody());
        verify(retrievalService).getAllMarios();
    }

    @Test
    void getAllSentMariosByEmployeeId_ShouldReturnListOfMarios() {
        // Given
        String employeeId = "12345";
        List<Marios> expectedSentMariosList = List.of(new Marios(/* initialize with necessary values */));
        when(retrievalService.getAllSentMariosByEmployeeId(employeeId)).thenReturn(expectedSentMariosList);

        // When
        ResponseEntity<List<Marios>> response = mariosService.getAllSentMariosByEmployeeId(employeeId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedSentMariosList, response.getBody());
        verify(retrievalService).getAllSentMariosByEmployeeId(employeeId);
    }

    @Test
    void getAllReceiveMariosByEmployeeId_ShouldReturnListOfMarios() {
        // Given
        String employeeId = "12345";
        List<Marios> expectedReceiveMariosList = List.of(new Marios(/* initialize with necessary values */));
        when(retrievalService.getAllReceiveMariosByEmployeeId(employeeId)).thenReturn(expectedReceiveMariosList);

        // When
        ResponseEntity<List<Marios>> response = mariosService.getAllReceiveMariosByEmployeeId(employeeId);

        // Then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedReceiveMariosList, response.getBody());
        verify(retrievalService).getAllReceiveMariosByEmployeeId(employeeId);
    }

}

package com.deloitte.ads.services;

import com.deloitte.ads.exceptions.EmployeeNotFoundException;
import com.deloitte.ads.exceptions.MariosNotFoundException;
import com.deloitte.ads.exceptions.SelfMariosException;
import com.deloitte.ads.models.Employee;
import com.deloitte.ads.models.Marios;
import com.deloitte.ads.models.ReactionType;
import com.deloitte.ads.repositories.impl.MongoMariosRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MariosServiceTest {
    @Mock
    private MongoMariosRepositoryImpl mariosRepository;

    @Mock
    private EmployeeService employeeService;

    private MariosService mariosService;

    private Employee sender;
    private Employee receiver;
    private Marios marios;

    @BeforeEach //todo: remove beforeEach | not in this case
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mariosService = new MariosService(mariosRepository, employeeService);

        sender = Employee.builder().firstName("Sender").build();
        receiver = Employee.builder().firstName("Receiver").build();

        marios = Marios.builder()
                .id(UUID.randomUUID())
                .message("Test message")
                .reaction(ReactionType.GOOD_JOB)
                .sender(sender)
                .receiver(receiver)
                .build();
    }

    @Test
    void addMarios_WhenEmployeesExist_ShouldSaveMarios() {
        // Arrange
        when(employeeService.isEmployeeExist(sender)).thenReturn(true);
        when(employeeService.isEmployeeExist(receiver)).thenReturn(true);
        doNothing().when(mariosRepository).saveMarios(any(Marios.class));

        // Act
        assertDoesNotThrow(() -> mariosService.addMarios(sender, receiver, "Test message", ReactionType.GOOD_JOB));

        // Assert
        ArgumentCaptor<Marios> mariosCaptor = ArgumentCaptor.forClass(Marios.class);
        verify(mariosRepository, times(1)).saveMarios(mariosCaptor.capture());

        Marios savedMarios = mariosCaptor.getValue();
        assertNotNull(savedMarios);
        assertEquals(sender, savedMarios.getSender());
        assertEquals(receiver, savedMarios.getReceiver());
        assertEquals("Test message", savedMarios.getMessage());
        assertEquals(ReactionType.GOOD_JOB, savedMarios.getReaction());
    }


    @Test
    void addMarios_WhenSenderEmployeeDoesNotExist_ShouldThrowEmployeeNotFoundException() {
        // Arrange
        when(employeeService.isEmployeeExist(sender)).thenReturn(false);

        // Act and Assert
        assertThrows(EmployeeNotFoundException.class, () ->
                mariosService.addMarios(sender, receiver, "Test message", ReactionType.GOOD_JOB));
    }

    @Test
    void addMarios_WhenReceiverEmployeeDoesNotExist_ShouldThrowEmployeeNotFoundException() throws EmployeeNotFoundException, SelfMariosException {
        // Arrange
        when(employeeService.isEmployeeExist(sender)).thenReturn(true);
        when(employeeService.isEmployeeExist(receiver)).thenReturn(false);

        // Act and Assert
        assertThrows(EmployeeNotFoundException.class, () ->
                mariosService.addMarios(sender, receiver, "Test message", ReactionType.GOOD_JOB));
    }

    @Test
    void addMarios_WhenSenderAndReceiverAreTheSame_ShouldThrowSelfMariosException() {
        // Arrange
        when(employeeService.isEmployeeExist(sender)).thenReturn(true);
        when(employeeService.isEmployeeExist(receiver)).thenReturn(true);

        // Act and Assert
        assertThrows(SelfMariosException.class, () ->
                mariosService.addMarios(sender, sender, "Test message", ReactionType.GOOD_JOB));
    }

    @Test
    void getMariosById_WhenMariosExist_ShouldReturnMarios() throws MariosNotFoundException {
        // Arrange
        UUID id = marios.getId();
        when(mariosRepository.getMariosById(id)).thenReturn(Optional.of(marios));

        // Act
        Marios result = mariosService.getMariosById(id);

        // Assert
        assertEquals(marios, result);
    }

    @Test
    void getMariosById_WhenMariosDoNotExist_ShouldThrowMariosNotFoundException() {
        // Arrange
        UUID id = UUID.randomUUID();
        when(mariosRepository.getMariosById(id)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(MariosNotFoundException.class, () ->
                mariosService.getMariosById(id));
    }

    @Test
    void updateMarios_ShouldCallRepositoryUpdateMarios() {
        // Act
        mariosService.updateMarios(marios);

        // Assert
        verify(mariosRepository, times(1)).updateMarios(marios);
    }

    @Test
    void deleteMarios_ShouldCallRepositoryDeleteMarios() {
        // Act
        mariosService.deleteMarios(marios);

        // Assert
        verify(mariosRepository, times(1)).deleteMarios(marios);
    }

    @Test
    void getAllMarios_ShouldReturnListOfMarios() {
        // Arrange
        List<Marios> mariosList = Arrays.asList(marios);
        when(mariosRepository.getAllMarios()).thenReturn(mariosList);

        // Act
        List<Marios> result = mariosService.getAllMarios();

        // Assert
        assertEquals(mariosList, result);
    }
}


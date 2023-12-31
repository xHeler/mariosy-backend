package com.deloitte.ads.repositories;

import com.deloitte.ads.models.Marios;
import com.deloitte.ads.repositories.impl.LocalMariosRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class LocalMariosRepositoryImplTest {

    //todo: given/when/then
    @Mock
    private Marios marios;

    private LocalMariosRepositoryImpl mariosRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mariosRepository = new LocalMariosRepositoryImpl();
    }

    @Test
    void should_AddMariosToRepository_When_SaveMarios() {
        // Act
        mariosRepository.saveMarios(marios);

        // Assert
        assertTrue(mariosRepository.getMariosMap().containsValue(marios));
    }

    @Test
    void should_ReturnMarios_When_GetMariosById_IfMariosExists() {
        // Arrange
        marios = Marios.builder().build();
        UUID id = marios.getId();
        mariosRepository.saveMarios(marios);

        // Act
        Optional<Marios> result = mariosRepository.getMariosById(id);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(marios, result.get());
    }

    @Test
    void should_ReturnEmptyOptional_When_GetMariosById_IfMariosDoesNotExist() {
        // Arrange
        UUID id = UUID.randomUUID();

        // Act
        Optional<Marios> result = mariosRepository.getMariosById(id);

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void should_UpdateMariosInRepository_When_UpdateMarios() {
        // Arrange
        marios = Marios.builder().build();
        UUID id = marios.getId();
        mariosRepository.saveMarios(marios);

        // Act
        Marios updatedMarios = Marios.builder().id(id).message("Updated message").build();
        mariosRepository.updateMarios(updatedMarios);

        // Assert
        assertEquals(updatedMarios, mariosRepository.getMariosMap().get(id));
    }

    @Test
    void should_RemoveMariosFromRepository_When_DeleteMarios() {
        // Arrange
        UUID id = UUID.randomUUID();
        marios.setId(id);
        mariosRepository.saveMarios(marios);

        // Act
        mariosRepository.deleteMarios(marios);

        // Assert
        assertFalse(mariosRepository.getMariosMap().containsKey(id));
    }

    @Test
    void should_ReturnListOfMarios_When_GetAllMarios_IfMariosExist() {
        // Arrange
        Marios marios1 = Marios.builder().message("Message 1").build();
        Marios marios2 = Marios.builder().message("Message 2").build();
        mariosRepository.saveMarios(marios1);
        mariosRepository.saveMarios(marios2);

        // Act
        List<Marios> result = mariosRepository.getAllMarios();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(marios1));
        assertTrue(result.contains(marios2));
    }

    @Test
    void should_ReturnEmptyList_When_GetAllMarios_IfNoMariosExist() {
        // Act
        List<Marios> result = mariosRepository.getAllMarios();

        // Assert
        assertTrue(result.isEmpty());
    }
}

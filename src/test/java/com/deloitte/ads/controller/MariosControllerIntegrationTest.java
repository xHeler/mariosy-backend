package com.deloitte.ads.controller;

import com.deloitte.ads.dto.MariosDto;
import com.deloitte.ads.factories.EmployeeFactory;
import com.deloitte.ads.factories.MariosFactory;
import com.deloitte.ads.models.Employee;
import com.deloitte.ads.models.Marios;
import com.deloitte.ads.models.ReactionType;
import com.deloitte.ads.services.MariosService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(MariosController.class)
public class MariosControllerIntegrationTest {

    @MockBean
    private MariosService mariosService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getAllMarios_ShouldReturnListOfMarios() throws Exception {
        // Given
        Employee sender = EmployeeFactory.createEmployee("John", "Doe");
        Employee receiver = EmployeeFactory.createEmployee("Jane", "Smith");
        ReactionType reaction = ReactionType.IMPRESSIVE;

        List<Marios> mariosList = List.of(
                MariosFactory.createMarios(sender, receiver, "message 1", reaction),
                MariosFactory.createMarios(sender, receiver, "message 2", reaction),
                MariosFactory.createMarios(sender, receiver, "message 3", reaction)
        );
        when(mariosService.getAllMarios()).thenReturn(ResponseEntity.ok(mariosList));

        // When & Then
        mockMvc.perform(get("/api/marios"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].sender.id").exists())
                .andExpect(jsonPath("$[0].sender.firstName").value("John"))
                .andExpect(jsonPath("$[0].sender.lastName").value("Doe"))
                .andExpect(jsonPath("$[0].receiver.id").exists())
                .andExpect(jsonPath("$[0].receiver.firstName").value("Jane"))
                .andExpect(jsonPath("$[0].receiver.lastName").value("Smith"))
                .andExpect(jsonPath("$[0].message").value("message 1"))
                .andExpect(jsonPath("$[0].reaction").value("IMPRESSIVE"))
                .andExpect(jsonPath("$[1].id").exists())
                .andExpect(jsonPath("$[1].sender.id").exists())
                .andExpect(jsonPath("$[1].sender.firstName").value("John"))
                .andExpect(jsonPath("$[1].sender.lastName").value("Doe"))
                .andExpect(jsonPath("$[1].receiver.id").exists())
                .andExpect(jsonPath("$[1].receiver.firstName").value("Jane"))
                .andExpect(jsonPath("$[1].receiver.lastName").value("Smith"))
                .andExpect(jsonPath("$[1].message").value("message 2"))
                .andExpect(jsonPath("$[1].reaction").value("IMPRESSIVE"));

        // Verify
        verify(mariosService).getAllMarios();
    }

    @Test
    void getAllSentMariosByEmployeeId_ShouldReturnListOfMarios() throws Exception {
        // Given
        Employee sender = EmployeeFactory.createEmployee("John", "Doe");
        Employee receiver = EmployeeFactory.createEmployee("Jane", "Smith");
        ReactionType reaction = ReactionType.THANK_YOU;
        String employeeId = sender.getId().toString();

        List<Marios> sentMariosList = List.of(
                MariosFactory.createMarios(sender, receiver, "message 1", reaction),
                MariosFactory.createMarios(sender, receiver, "message 2", reaction),
                MariosFactory.createMarios(sender, receiver, "message 3", reaction)
        );
        when(mariosService.getAllSentMariosByEmployeeId(employeeId)).thenReturn(ResponseEntity.ok(sentMariosList));

        // When & Then
        mockMvc.perform(get("/api/marios/sent/{employeeId}", employeeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].sender.id").exists())
                .andExpect(jsonPath("$[0].sender.firstName").value("John"))
                .andExpect(jsonPath("$[0].sender.lastName").value("Doe"))
                .andExpect(jsonPath("$[0].receiver.id").exists())
                .andExpect(jsonPath("$[0].receiver.firstName").value("Jane"))
                .andExpect(jsonPath("$[0].receiver.lastName").value("Smith"))
                .andExpect(jsonPath("$[0].message").value("message 1"))
                .andExpect(jsonPath("$[0].reaction").value("THANK_YOU"))
                .andExpect(jsonPath("$[1].id").exists())
                .andExpect(jsonPath("$[1].sender.id").exists())
                .andExpect(jsonPath("$[1].sender.firstName").value("John"))
                .andExpect(jsonPath("$[1].sender.lastName").value("Doe"))
                .andExpect(jsonPath("$[1].receiver.id").exists())
                .andExpect(jsonPath("$[1].receiver.firstName").value("Jane"))
                .andExpect(jsonPath("$[1].receiver.lastName").value("Smith"))
                .andExpect(jsonPath("$[1].message").value("message 2"))
                .andExpect(jsonPath("$[1].reaction").value("THANK_YOU"));

        // Verify
        verify(mariosService).getAllSentMariosByEmployeeId(employeeId);
    }

    @Test
    void getAllReceiveMariosByEmployeeId_ShouldReturnListOfMarios() throws Exception {
        // Given
        Employee sender = EmployeeFactory.createEmployee("John", "Doe");
        Employee receiver = EmployeeFactory.createEmployee("Jane", "Smith");
        ReactionType reaction = ReactionType.THANK_YOU;
        String employeeId = receiver.getId().toString();

        List<Marios> receiveMariosList = List.of(
                MariosFactory.createMarios(sender, receiver, "message 1", reaction),
                MariosFactory.createMarios(sender, receiver, "message 2", reaction),
                MariosFactory.createMarios(sender, receiver, "message 3", reaction)
        );
        when(mariosService.getAllReceiveMariosByEmployeeId(employeeId)).thenReturn(ResponseEntity.ok(receiveMariosList));

        // When & Then
        mockMvc.perform(get("/api/marios/receive/{employeeId}", employeeId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").exists())
                .andExpect(jsonPath("$[0].sender.id").exists())
                .andExpect(jsonPath("$[0].sender.firstName").value("John"))
                .andExpect(jsonPath("$[0].sender.lastName").value("Doe"))
                .andExpect(jsonPath("$[0].receiver.id").exists())
                .andExpect(jsonPath("$[0].receiver.firstName").value("Jane"))
                .andExpect(jsonPath("$[0].receiver.lastName").value("Smith"))
                .andExpect(jsonPath("$[0].message").value("message 1"))
                .andExpect(jsonPath("$[0].reaction").value("THANK_YOU"))
                .andExpect(jsonPath("$[1].id").exists())
                .andExpect(jsonPath("$[1].sender.id").exists())
                .andExpect(jsonPath("$[1].sender.firstName").value("John"))
                .andExpect(jsonPath("$[1].sender.lastName").value("Doe"))
                .andExpect(jsonPath("$[1].receiver.id").exists())
                .andExpect(jsonPath("$[1].receiver.firstName").value("Jane"))
                .andExpect(jsonPath("$[1].receiver.lastName").value("Smith"))
                .andExpect(jsonPath("$[1].message").value("message 2"))
                .andExpect(jsonPath("$[1].reaction").value("THANK_YOU"));

        // Verify
        verify(mariosService).getAllReceiveMariosByEmployeeId(employeeId);
    }

    @Test
    void addMarios_ShouldReturnOkStatus() throws Exception {
        // Given
        ObjectMapper objectMapper = new ObjectMapper();

        String senderId = UUID.randomUUID().toString();
        String receiverId = UUID.randomUUID().toString();
        String message = "Sample message";
        ReactionType reaction = ReactionType.THANK_YOU;

        MariosDto mariosDto = MariosDto.builder().senderId(senderId)
                .receiversId(List.of(receiverId)).message(message).reaction(reaction).build();
        when(mariosService.addMariosFromDto(any(MariosDto.class))).thenReturn(ResponseEntity.ok().build());

        // When & Then
        mockMvc.perform(post("/api/marios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(mariosDto)))
                .andExpect(status().isOk());

        // Verify
        verify(mariosService).addMariosFromDto(any(MariosDto.class));
    }

    @Test
    void removeMariosByMariosId_ShouldReturnOkStatus() throws Exception {
        // Given
        String mariosId = String.valueOf(UUID.randomUUID());
        when(mariosService.removeMariosById(mariosId)).thenReturn(ResponseEntity.ok().build());

        // When & Then
        mockMvc.perform(delete("/api/marios/{mariosId}", mariosId))
                .andExpect(status().isOk());

        // Verify
        verify(mariosService).removeMariosById(mariosId);
    }

    @Test
    void updateMarios_ShouldReturnOkStatus() throws Exception {
        // Given
        String mariosId = String.valueOf(UUID.randomUUID());
        when(mariosService.updateMariosById(mariosId)).thenReturn(ResponseEntity.ok().build());

        // When & Then
        mockMvc.perform(put("/api/marios/{mariosId}", mariosId))
                .andExpect(status().isOk());

        // Verify
        verify(mariosService).updateMariosById(mariosId);
    }
}

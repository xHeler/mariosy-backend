package com.deloitte.ads.controller;

import com.deloitte.ads.dto.MariosDto;
import com.deloitte.ads.dto.MariosListDto;
import com.deloitte.ads.factories.EmployeeFactory;
import com.deloitte.ads.factories.MariosFactory;
import com.deloitte.ads.factories.MariosListDtoFactory;
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
    void should_ReturnOkStatus_When_AddMarios() throws Exception {
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
    void should_ReturnOkStatus_When_RemoveMariosByMariosId() throws Exception {
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
    void should_ReturnOkStatus_When_UpdateMarios() throws Exception {
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

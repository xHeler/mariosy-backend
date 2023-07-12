package com.deloitte.ads.controller;

import com.deloitte.ads.dto.MariosDto;
import com.deloitte.ads.models.Employee;
import com.deloitte.ads.models.Marios;
import com.deloitte.ads.models.ReactionType;
import com.deloitte.ads.services.EmployeeService;
import com.deloitte.ads.services.MariosService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@WebMvcTest(MariosController.class)
public class MariosControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MariosService mariosService;

    @MockBean
    private EmployeeService employeeService;

    private Employee sender;
    private Employee receiver;

    private Marios marios1;
    private Marios marios2;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        sender = Employee.builder().build();
        receiver = Employee.builder().build();

        marios1 = Marios
                .builder()
                .sender(sender)
                .receiver(receiver)
                .message("test")
                .reaction(ReactionType.GOOD_JOB)
                .build();
        marios2 = Marios
                .builder()
                .sender(sender)
                .receiver(receiver)
                .message("test2")
                .reaction(ReactionType.AWESOME)
                .build();
    }

    @Test
    public void testGetAllMarios() throws Exception {
        List<Marios> mariosList = Arrays.asList(marios1, marios2);

        when(mariosService.getAllMarios()).thenReturn(mariosList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/marios")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(marios1.getId().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].receiver.id").value(marios1.getReceiver().getId().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].sender.id").value(marios1.getSender().getId().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].message").value(marios1.getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].reaction").value(marios1.getReaction().name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(marios2.getId().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].receiver.id").value(marios2.getReceiver().getId().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].sender.id").value(marios2.getSender().getId().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].message").value(marios2.getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].reaction").value(marios2.getReaction().name()));
    }

    @Test
    public void testGetAllSentMariosByEmployeeId() throws Exception {
        List<Marios> mariosList = Arrays.asList(marios1, marios2);

        String senderId = String.valueOf(sender.getId());
        when(mariosService.getAllSentMariosByEmployeeId(eq(senderId))).thenReturn(mariosList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/marios/sent/{id}", senderId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(marios1.getId().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].message").value(marios1.getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].reaction").value(marios1.getReaction()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(marios2.getId().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].message").value(marios2.getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].reaction").value(marios2.getReaction()));
    }

    @Test
    public void testGetAllReceiveMariosByEmployeeId() throws Exception {
        String receiverId = String.valueOf(receiver.getId());
        List<Marios> mariosList = Arrays.asList(marios1, marios2);

        when(mariosService.getAllReceiveMariosByEmployeeId(eq(receiverId))).thenReturn(mariosList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/marios/receive/{id}", receiverId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(marios1.getId().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].message").value(marios1.getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].reaction").value(marios1.getReaction().name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(marios2.getId().toString()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].message").value(marios2.getMessage()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].reaction").value(marios2.getReaction().name()));
    }

    @Test
    public void testAddMarios() throws Exception {

        MariosDto mariosDto = MariosDto
                .builder()
                .message("test")
                .senderId(sender.getId().toString())
                .receiversId(Collections.singletonList(receiver.getId().toString()))
                .reaction(ReactionType.IMPRESSIVE)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String mariosDtoJson = objectMapper.writeValueAsString(mariosDto);

        when(employeeService.getEmployeeById(sender.getId())).thenReturn(sender);
        when(employeeService.getAllEmployeesByIds(mariosDto.getReceiversId())).thenReturn(List.of(receiver));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/marios")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mariosDtoJson))
                .andExpect(MockMvcResultMatchers.status().isOk());

        verify(mariosService).addMarios(sender, Collections.singletonList(receiver), mariosDto.getMessage(), mariosDto.getReaction());
    }

}

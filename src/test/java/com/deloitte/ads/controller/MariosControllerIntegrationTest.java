package com.deloitte.ads.controller;

import com.deloitte.ads.dto.MariosDto;
import com.deloitte.ads.models.Employee;
import com.deloitte.ads.models.Marios;
import com.deloitte.ads.models.ReactionType;
import com.deloitte.ads.services.EmployeeService;
import com.deloitte.ads.services.MariosService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
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
import static org.mockito.Mockito.*;

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


    @Test
    public void testRemoveMarios() throws Exception {
        UUID mariosId = marios1.getId();

        when(mariosService.getMariosById(mariosId)).thenReturn(marios1);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/marios/{id}", mariosId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Verify that the deleteMarios method is called with the correct marios ID
        verify(mariosService).deleteMarios(eq(marios1));
    }

    @Test
    public void testUpdateMarios() throws Exception {
        UUID mariosId = marios1.getId();

        when(mariosService.getMariosById(mariosId)).thenReturn(marios1);

        mockMvc.perform(MockMvcRequestBuilders.put("/api/v1/marios/{id}", mariosId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());

        // Verify that the updateMarios method is called with the correct marios ID
        verify(mariosService).updateMarios(eq(marios1));
    }

    @Test
    public void testGetAllSentMariosByEmployeeId() throws Exception {
        List<Marios> sentMarios = Arrays.asList(marios1, marios2);
        String employeeId = sender.getId().toString();

        when(mariosService.getAllSentMariosByEmployeeId(employeeId)).thenReturn(sentMarios);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/marios/sent/{id}", employeeId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].message", Matchers.is("test")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].reaction", Matchers.is("GOOD_JOB")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].message", Matchers.is("test2")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].reaction", Matchers.is("AWESOME")));

        verify(mariosService, Mockito.times(1)).getAllSentMariosByEmployeeId(employeeId);
        verifyNoMoreInteractions(mariosService);
    }

    @Test
    public void testGetAllReceiveMariosByEmployeeId() throws Exception {
        List<Marios> receiveMarios = Arrays.asList(marios1, marios2);
        String employeeId = receiver.getId().toString();

        Mockito.when(mariosService.getAllReceiveMariosByEmployeeId(employeeId)).thenReturn(receiveMarios);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/marios/receive/{id}", employeeId))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].message", Matchers.is("test")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].reaction", Matchers.is("GOOD_JOB")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].message", Matchers.is("test2")))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].reaction", Matchers.is("AWESOME")));

        Mockito.verify(mariosService, Mockito.times(1)).getAllReceiveMariosByEmployeeId(employeeId);
        verifyNoMoreInteractions(mariosService);
    }

}

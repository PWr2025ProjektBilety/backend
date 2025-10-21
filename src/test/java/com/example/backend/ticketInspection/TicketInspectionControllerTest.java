package com.example.backend.ticketInspection;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.core.authority.AuthorityUtils.createAuthorityList;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TicketInspectionController.class)
public class TicketInspectionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private TicketInspectionService ticketInspectionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "INSPECTOR")
    void shouldReturnOkAndTrue_whenValidateTicketIsSuccessful() throws Exception {
        InspectTicketRequestDTO request = new InspectTicketRequestDTO();
        Mockito.when(ticketInspectionService.validateTicket(any(), eq("inspectorUser"))).thenReturn(true);

        mockMvc.perform(post("/api/ticket-inspection")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(jwt().authorities(createAuthorityList("ROLE_INSPECTOR"))
                                .jwt(jwt -> jwt.subject("inspectorUser"))))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    @WithMockUser(roles = "INSPECTOR")
    void shouldReturnNotFound_whenValidateTicketThrowsException() throws Exception {
        InspectTicketRequestDTO request = new InspectTicketRequestDTO();
        Mockito.when(ticketInspectionService.validateTicket(any(), eq("inspectorUser")))
                .thenThrow(new RuntimeException("Not found"));

        mockMvc.perform(post("/api/ticket-inspection")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(jwt().authorities(createAuthorityList("ROLE_INSPECTOR"))
                                .jwt(jwt -> jwt.subject("inspectorUser"))))
                .andExpect(status().isNotFound());
    }

}

package com.example.backend.ticketInspection;

import com.example.backend.controller.ticketInspection.VehicleTicketValidationLockController;
import com.example.backend.dto.ticketInspection.VehicleTicketValidationLockRequestDTO;
import com.example.backend.service.purchasedTicket.VehicleTicketValidationLockService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneOffset;

import static org.mockito.ArgumentMatchers.*;
import static org.springframework.security.core.authority.AuthorityUtils.createAuthorityList;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(VehicleTicketValidationLockController.class)
@Import(VehicleTicketValidationLockControllerTest.FixedClockTestConfig.class)
public class VehicleTicketValidationLockControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private VehicleTicketValidationLockService lockService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "INSPECTOR")
    void status_shouldReturnLockedTrue_whenServiceReportsLock() throws Exception {
        Mockito.when(lockService.getLockedUntil("BUS1")).thenReturn(Instant.parse("2026-01-01T00:20:00Z"));

        mockMvc.perform(get("/api/vehicle-ticket-validation-lock/BUS1")
                        .with(jwt().authorities(createAuthorityList("ROLE_INSPECTOR"))
                                .jwt(jwt -> jwt.subject("inspectorUser"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.locked").value(true))
                .andExpect(jsonPath("$.lockedUntilEpochSeconds").value(Instant.parse("2026-01-01T00:20:00Z").getEpochSecond()))
                .andExpect(jsonPath("$.remainingSeconds").value(20 * 60));
    }

    @Test
    @WithMockUser(roles = "INSPECTOR")
    void lock_shouldReturnStatus() throws Exception {
        VehicleTicketValidationLockRequestDTO req = new VehicleTicketValidationLockRequestDTO();
        req.setVehicleId("BUS1");
        req.setDurationMinutes(15);

        Mockito.when(lockService.lock(eq("BUS1"), any())).thenReturn(Instant.parse("2026-01-01T00:15:00Z"));

        mockMvc.perform(post("/api/vehicle-ticket-validation-lock/lock")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req))
                        .with(jwt().authorities(createAuthorityList("ROLE_INSPECTOR"))
                                .jwt(jwt -> jwt.subject("inspectorUser"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.locked").value(true))
                .andExpect(jsonPath("$.remainingSeconds").value(15 * 60));
    }

    static class FixedClockTestConfig {
        @Bean
        Clock clock() {
            return Clock.fixed(Instant.parse("2026-01-01T00:00:00Z"), ZoneOffset.UTC);
        }
    }
}

package com.example.backend.ticketInspection;

import com.example.backend.purchasedticket.model.PurchasedTicket;
import com.example.backend.purchasedticket.repository.PurchasedTicketRepository;
import com.example.backend.user.model.TicketInspector;
import com.example.backend.user.repository.InspectorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TicketInspectionServiceTest {

    @InjectMocks
    private TicketInspectionService ticketInspectionService;

    @Mock
    private PurchasedTicketRepository purchasedTicketRepository;

    @Mock
    private InspectorRepository inspectorRepository;

    @Mock
    private PurchasedTicket purchasedTicket;

    @Mock
    private TicketInspector ticketInspector;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldReturnTrue_whenTicketAndInspectorFoundAndAccepted() {
        InspectTicketRequestDTO dto = new InspectTicketRequestDTO();
        dto.setTicketCode("CODE123");
        dto.setVehicleId("BUS1");

        when(purchasedTicketRepository.findByCode("CODE123")).thenReturn(Optional.of(purchasedTicket));
        when(inspectorRepository.findByLogin("inspectorUser")).thenReturn(Optional.of(ticketInspector));
        when(purchasedTicket.accept(ticketInspector, "BUS1")).thenReturn(true);

        boolean result = ticketInspectionService.validateTicket(dto, "inspectorUser");

        assertTrue(result);
        verify(purchasedTicketRepository).findByCode("CODE123");
        verify(inspectorRepository).findByLogin("inspectorUser");
        verify(purchasedTicket).accept(ticketInspector, "BUS1");
    }

    @Test
    void shouldReturnFalse_whenTicketAndInspectorFoundAndTicketNotAccepted() {
        InspectTicketRequestDTO dto = new InspectTicketRequestDTO();
        dto.setTicketCode("CODE123");
        dto.setVehicleId("BUS1");

        when(purchasedTicketRepository.findByCode("CODE123")).thenReturn(Optional.of(purchasedTicket));
        when(inspectorRepository.findByLogin("inspectorUser")).thenReturn(Optional.of(ticketInspector));
        when(purchasedTicket.accept(ticketInspector, "BUS1")).thenReturn(false);

        boolean result = ticketInspectionService.validateTicket(dto, "inspectorUser");

        assertFalse(result);
        verify(purchasedTicketRepository).findByCode("CODE123");
        verify(inspectorRepository).findByLogin("inspectorUser");
        verify(purchasedTicket).accept(ticketInspector, "BUS1");
    }

    @Test
    void shouldThrowException_whenTicketNotFound() {
        InspectTicketRequestDTO dto = new InspectTicketRequestDTO();
        dto.setTicketCode("NOT_FOUND");
        dto.setVehicleId("BUS1");

        when(purchasedTicketRepository.findByCode("NOT_FOUND")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                ticketInspectionService.validateTicket(dto, "inspectorUser")
        );
        assertTrue(ex.getMessage().contains("Ticket not found"));
        verify(purchasedTicketRepository).findByCode("NOT_FOUND");
        verifyNoInteractions(inspectorRepository);
    }

    @Test
    void shouldThrowException_whenInspectorNotFound() {
        InspectTicketRequestDTO dto = new InspectTicketRequestDTO();
        dto.setTicketCode("CODE123");
        dto.setVehicleId("BUS1");

        when(purchasedTicketRepository.findByCode("CODE123")).thenReturn(Optional.of(purchasedTicket));
        when(inspectorRepository.findByLogin("inspectorUser")).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                ticketInspectionService.validateTicket(dto, "inspectorUser")
        );
        assertTrue(ex.getMessage().contains("Ticket inspector not found"));
        verify(purchasedTicketRepository).findByCode("CODE123");
        verify(inspectorRepository).findByLogin("inspectorUser");
        verifyNoMoreInteractions(purchasedTicket);
    }

}

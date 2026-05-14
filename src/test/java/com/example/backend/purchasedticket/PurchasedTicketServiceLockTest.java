package com.example.backend.purchasedticket;

import com.example.backend.model.purchasedTicket.PurchasedTicket;
import com.example.backend.model.purchasedTicket.TicketValidationRequest;
import com.example.backend.repository.purchasedTicket.PurchasedTicketRepository;
import com.example.backend.service.purchasedTicket.PurchasedTicketService;
import com.example.backend.service.purchasedTicket.VehicleTicketValidationLockService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PurchasedTicketServiceLockTest {

    @Mock
    private PurchasedTicketRepository purchasedTicketRepository;

    @Mock
    private VehicleTicketValidationLockService lockService;

    @InjectMocks
    private PurchasedTicketService purchasedTicketService;

    @BeforeEach
    void setUp() {
        // other autowired deps are not used by validateTicket; Mockito will set them to null and that's fine.
    }

    @Test
    void validateTicket_shouldReturnFalse_whenVehicleIsLocked() {
        TicketValidationRequest req = new TicketValidationRequest();
        req.setTicketId("T1");
        req.setVehicleId("BUS1");

        when(lockService.isLocked("BUS1")).thenReturn(true);

        boolean result = purchasedTicketService.validateTicket(req);

        assertFalse(result);
        verify(lockService).isLocked("BUS1");
        verifyNoInteractions(purchasedTicketRepository);
    }

    @Test
    void validateTicket_shouldProceed_whenVehicleNotLocked() {
        TicketValidationRequest req = new TicketValidationRequest();
        req.setTicketId("T1");
        req.setVehicleId("BUS1");

        PurchasedTicket ticket = mock(PurchasedTicket.class);

        when(lockService.isLocked("BUS1")).thenReturn(false);
        when(purchasedTicketRepository.findByCode("T1")).thenReturn(Optional.of(ticket));
        when(ticket.validate("BUS1")).thenReturn(true);

        boolean result = purchasedTicketService.validateTicket(req);

        assertTrue(result);
        verify(lockService).isLocked("BUS1");
        verify(purchasedTicketRepository).findByCode("T1");
        verify(ticket).validate("BUS1");
        verify(purchasedTicketRepository).save(ticket);
    }
}

package com.example.backend.ticketInspection;

import com.example.backend.dto.ticketInspection.InspectTicketBodyDTO;
import com.example.backend.dto.ticketInspection.InspectTicketRequestDTO;
import com.example.backend.dto.ticketInspection.InspectTicketResponseDTO;
import com.example.backend.model.purchasedTicket.PurchasedTicket;
import com.example.backend.model.purchasedTicket.PurchasedTicketPeriodic;
import com.example.backend.model.purchasedTicket.PurchasedTicketSingleRide;
import com.example.backend.model.purchasedTicket.PurchasedTicketTimeBased;
import com.example.backend.repository.purchasedTicket.PurchasedTicketRepository;
import com.example.backend.service.ticketInspection.QrPayloadService;
import com.example.backend.model.user.TicketInspector;
import com.example.backend.repository.user.InspectorRepository;
import com.example.backend.service.ticketInspection.TicketInspectionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
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
    private QrPayloadService qrPayloadService;

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

    @Test
    void inspectTicket_shouldReturnInvalidQr_whenPayloadInvalid() {
        when(qrPayloadService.extractTicketCodeOrNull("QR_PAYLOAD")).thenReturn(null);

        InspectTicketResponseDTO res = ticketInspectionService.inspectTicket(
                "QR_PAYLOAD",
                new InspectTicketBodyDTO(),
                "inspectorUser"
        );

        assertEquals("invalid", res.getStatus());
        assertEquals("invalid-qr", res.getReason());
        verifyNoInteractions(inspectorRepository);
        verifyNoInteractions(purchasedTicketRepository);
    }

    @Test
    void inspectTicket_shouldReturnTicketNotFound_whenNoTicketInDb() {
        when(qrPayloadService.extractTicketCodeOrNull("QR_PAYLOAD")).thenReturn("CODE123");
        when(inspectorRepository.findByLogin("inspectorUser")).thenReturn(Optional.of(ticketInspector));
        when(purchasedTicketRepository.findByCode("CODE123")).thenReturn(Optional.empty());

        InspectTicketBodyDTO body = new InspectTicketBodyDTO();
        body.setVehicleId("BUS1");

        InspectTicketResponseDTO res = ticketInspectionService.inspectTicket("QR_PAYLOAD", body, "inspectorUser");

        assertEquals("invalid", res.getStatus());
        assertEquals("ticket-not-found", res.getReason());
    }

    @Test
    void inspectTicket_shouldReturnNotValidated_forSingleRide() {
        PurchasedTicketSingleRide singleRide = mock(PurchasedTicketSingleRide.class);

        when(qrPayloadService.extractTicketCodeOrNull("QR_PAYLOAD")).thenReturn("CODE123");
        when(inspectorRepository.findByLogin("inspectorUser")).thenReturn(Optional.of(ticketInspector));
        when(purchasedTicketRepository.findByCode("CODE123")).thenReturn(Optional.of(singleRide));
        when(singleRide.isValidated()).thenReturn(false);

        InspectTicketBodyDTO body = new InspectTicketBodyDTO();
        body.setVehicleId("BUS1");

        InspectTicketResponseDTO res = ticketInspectionService.inspectTicket("QR_PAYLOAD", body, "inspectorUser");

        assertEquals("invalid", res.getStatus());
        assertEquals("ticket-not-validated", res.getReason());
    }

    @Test
    void inspectTicket_shouldReturnNotValidForVehicle_forSingleRide() {
        PurchasedTicketSingleRide singleRide = mock(PurchasedTicketSingleRide.class);

        when(qrPayloadService.extractTicketCodeOrNull("QR_PAYLOAD")).thenReturn("CODE123");
        when(inspectorRepository.findByLogin("inspectorUser")).thenReturn(Optional.of(ticketInspector));
        when(purchasedTicketRepository.findByCode("CODE123")).thenReturn(Optional.of(singleRide));
        when(singleRide.isValidated()).thenReturn(true);
        when(singleRide.getVehicleId()).thenReturn("BUS2");

        InspectTicketBodyDTO body = new InspectTicketBodyDTO();
        body.setVehicleId("BUS1");

        InspectTicketResponseDTO res = ticketInspectionService.inspectTicket("QR_PAYLOAD", body, "inspectorUser");

        assertEquals("invalid", res.getStatus());
        assertEquals("ticket-not-valid-for-vehicle", res.getReason());
    }

    @Test
    void inspectTicket_shouldReturnExpired_forTimeBased() {
        PurchasedTicketTimeBased timeBased = mock(PurchasedTicketTimeBased.class);

        when(qrPayloadService.extractTicketCodeOrNull("QR_PAYLOAD")).thenReturn("CODE123");
        when(inspectorRepository.findByLogin("inspectorUser")).thenReturn(Optional.of(ticketInspector));
        when(purchasedTicketRepository.findByCode("CODE123")).thenReturn(Optional.of(timeBased));
        when(timeBased.isValidated()).thenReturn(true);
        when(timeBased.getExpirationDate()).thenReturn(LocalDateTime.now().minusMinutes(1));

        InspectTicketBodyDTO body = new InspectTicketBodyDTO();
        body.setVehicleId("BUS1");

        InspectTicketResponseDTO res = ticketInspectionService.inspectTicket("QR_PAYLOAD", body, "inspectorUser");

        assertEquals("invalid", res.getStatus());
        assertEquals("ticket-expired", res.getReason());
    }

    @Test
    void inspectTicket_shouldReturnValid_forPeriodic() {
        PurchasedTicketPeriodic periodic = mock(PurchasedTicketPeriodic.class);

        when(qrPayloadService.extractTicketCodeOrNull("QR_PAYLOAD")).thenReturn("CODE123");
        when(inspectorRepository.findByLogin("inspectorUser")).thenReturn(Optional.of(ticketInspector));
        when(purchasedTicketRepository.findByCode("CODE123")).thenReturn(Optional.of(periodic));

        when(periodic.getValidFrom()).thenReturn(LocalDateTime.now().minusDays(1));
        when(periodic.getValidTo()).thenReturn(LocalDateTime.now().plusDays(1));

        InspectTicketBodyDTO body = new InspectTicketBodyDTO();
        body.setVehicleId("BUS1");

        InspectTicketResponseDTO res = ticketInspectionService.inspectTicket("QR_PAYLOAD", body, "inspectorUser");

        assertEquals("valid", res.getStatus());
        assertEquals("", res.getReason());
    }

}

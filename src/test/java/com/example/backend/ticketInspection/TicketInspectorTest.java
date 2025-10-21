package com.example.backend.ticketInspection;

import com.example.backend.purchasedticket.model.PurchasedTicketPeriodic;
import com.example.backend.purchasedticket.model.PurchasedTicketSingleRide;
import com.example.backend.purchasedticket.model.PurchasedTicketTimeBased;
import com.example.backend.user.model.TicketInspector;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TicketInspectorTest {

    @Test
    void visitSingleRide_shouldReturnTrue_whenValidatedAndVehicleIdMatches() {
        TicketInspector inspector = new TicketInspector();
        PurchasedTicketSingleRide ticket = mock(PurchasedTicketSingleRide.class);

        when(ticket.isValidated()).thenReturn(true);
        when(ticket.getVehicleId()).thenReturn("BUS1");

        assertTrue(inspector.visit(ticket, "BUS1"));
    }

    @Test
    void visitSingleRide_shouldReturnFalse_whenNotValidated() {
        TicketInspector inspector = new TicketInspector();
        PurchasedTicketSingleRide ticket = mock(PurchasedTicketSingleRide.class);

        when(ticket.isValidated()).thenReturn(false);

        assertFalse(inspector.visit(ticket, "BUS1"));
    }

    @Test
    void visitSingleRide_shouldReturnFalse_whenVehicleIdDoesNotMatch() {
        TicketInspector inspector = new TicketInspector();
        PurchasedTicketSingleRide ticket = mock(PurchasedTicketSingleRide.class);

        when(ticket.isValidated()).thenReturn(true);
        when(ticket.getVehicleId()).thenReturn("BUS2");

        assertFalse(inspector.visit(ticket, "BUS1"));
    }

    @Test
    void visitTimeBased_shouldReturnTrue_whenValidatedAndNotExpired() {
        TicketInspector inspector = new TicketInspector();
        PurchasedTicketTimeBased ticket = mock(PurchasedTicketTimeBased.class);

        when(ticket.isValidated()).thenReturn(true);
        when(ticket.getExpirationDate()).thenReturn(LocalDateTime.now().plusMinutes(10));

        assertTrue(inspector.visit(ticket, "ANY"));
    }

    @Test
    void visitTimeBased_shouldReturnFalse_whenExpired() {
        TicketInspector inspector = new TicketInspector();
        PurchasedTicketTimeBased ticket = mock(PurchasedTicketTimeBased.class);

        when(ticket.isValidated()).thenReturn(true);
        when(ticket.getExpirationDate()).thenReturn(LocalDateTime.now().minusMinutes(1));

        assertFalse(inspector.visit(ticket, "ANY"));
    }

    @Test
    void visitTimeBased_shouldReturnFalse_whenNotValidated() {
        TicketInspector inspector = new TicketInspector();
        PurchasedTicketTimeBased ticket = mock(PurchasedTicketTimeBased.class);

        when(ticket.isValidated()).thenReturn(false);

        assertFalse(inspector.visit(ticket, "ANY"));
    }

    @Test
    void visitPeriodic_shouldReturnTrue_whenWithinValidPeriod() {
        TicketInspector inspector = new TicketInspector();
        PurchasedTicketPeriodic ticket = mock(PurchasedTicketPeriodic.class);

        when(ticket.getValidFrom()).thenReturn(LocalDateTime.now().minusDays(1));
        when(ticket.getValidTo()).thenReturn(LocalDateTime.now().plusDays(1));

        assertTrue(inspector.visit(ticket, "ANY"));
    }

    @Test
    void visitPeriodic_shouldReturnFalse_whenBeforeValidFrom() {
        TicketInspector inspector = new TicketInspector();
        PurchasedTicketPeriodic ticket = mock(PurchasedTicketPeriodic.class);

        when(ticket.getValidFrom()).thenReturn(LocalDateTime.now().plusDays(1));
        when(ticket.getValidTo()).thenReturn(LocalDateTime.now().plusDays(2));

        assertFalse(inspector.visit(ticket, "ANY"));
    }

    @Test
    void visitPeriodic_shouldReturnFalse_whenAfterValidTo() {
        TicketInspector inspector = new TicketInspector();
        PurchasedTicketPeriodic ticket = mock(PurchasedTicketPeriodic.class);

        when(ticket.getValidFrom()).thenReturn(LocalDateTime.now().minusDays(2));
        when(ticket.getValidTo()).thenReturn(LocalDateTime.now().minusDays(1));

        assertFalse(inspector.visit(ticket, "ANY"));
    }


}

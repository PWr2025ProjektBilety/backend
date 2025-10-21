package com.example.backend.purchasedticket;

import com.example.backend.purchasedticket.dto.NewTicketDTO;
import com.example.backend.purchasedticket.model.*;
import com.example.backend.ticket.model.TicketPeriodic;
import com.example.backend.ticket.model.TicketSingleRide;
import com.example.backend.ticket.model.TicketTimeBased;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PurchasedTicketFactoryTest {

    @Test
    void shouldCreateSingleRideTicket() {
        TicketSingleRide baseTicket = mock(TicketSingleRide.class);
        when(baseTicket.getPrice()).thenReturn(10.0);

        NewTicketDTO dto = new NewTicketDTO();
        dto.setTicketType(TicketType.SINGLE_RIDE_TICKET);
        dto.setBaseTicket(baseTicket);
        dto.setReduced(false);

        PurchasedTicket ticket = PurchasedTicketFactory.createPurchasedTicket(dto);

        assertInstanceOf(PurchasedTicketSingleRide.class, ticket);
        assertEquals(baseTicket, ((PurchasedTicketSingleRide) ticket).getSingleTicket());
        assertFalse(((PurchasedTicketSingleRide) ticket).isValidated());
        assertEquals(10.0, ticket.getFinalPrice());
        assertNotNull(ticket.getPurchaseDate());
    }

    @Test
    void shouldCreatePeriodicTicket() {
        TicketPeriodic baseTicket = mock(TicketPeriodic.class);
        when(baseTicket.getPrice()).thenReturn(20.0);
        when(baseTicket.getValidityPeriod()).thenReturn(30L);

        LocalDateTime start = LocalDateTime.now();

        NewTicketDTO dto = new NewTicketDTO();
        dto.setTicketType(TicketType.PERIODIC_TICKET);
        dto.setBaseTicket(baseTicket);
        dto.setStartTime(start);
        dto.setReduced(true);

        PurchasedTicket ticket = PurchasedTicketFactory.createPurchasedTicket(dto);

        assertInstanceOf(PurchasedTicketPeriodic.class, ticket);
        assertEquals(baseTicket, ((PurchasedTicketPeriodic) ticket).getPeriodicTicket());
        assertEquals(start, ((PurchasedTicketPeriodic) ticket).getValidFrom());
        assertEquals(start.plusDays(30), ((PurchasedTicketPeriodic) ticket).getValidTo());
        assertEquals(10.0, ticket.getFinalPrice()); // 50% discount
        assertNotNull(ticket.getPurchaseDate());
    }

    @Test
    void shouldCreateTimeBasedTicket() {
        TicketTimeBased baseTicket = mock(TicketTimeBased.class);
        when(baseTicket.getPrice()).thenReturn(15.0);

        NewTicketDTO dto = new NewTicketDTO();
        dto.setTicketType(TicketType.TIME_BASED_TICKET);
        dto.setBaseTicket(baseTicket);
        dto.setReduced(false);

        PurchasedTicket ticket = PurchasedTicketFactory.createPurchasedTicket(dto);

        assertInstanceOf(PurchasedTicketTimeBased.class, ticket);
        assertEquals(baseTicket, ((PurchasedTicketTimeBased) ticket).getTimeBasedTicket());
        assertFalse(((PurchasedTicketTimeBased) ticket).isValidated());
        assertEquals(15.0, ticket.getFinalPrice());
        assertNotNull(ticket.getPurchaseDate());
    }

    @Test
    void shouldReturnNullForUnknownType() {
        NewTicketDTO dto = new NewTicketDTO();
        dto.setTicketType(null);
        assertThrows(IllegalArgumentException.class, () -> PurchasedTicketFactory.createPurchasedTicket(dto));
    }

}

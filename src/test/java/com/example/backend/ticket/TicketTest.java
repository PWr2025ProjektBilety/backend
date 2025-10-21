package com.example.backend.ticket;

import com.example.backend.ticket.controller.TicketController;
import com.example.backend.ticket.model.Ticket;
import com.example.backend.ticket.model.TicketTimeBased;
import com.example.backend.ticket.model.TicketSingleRide;
import com.example.backend.ticket.model.TicketPeriodic;
import com.example.backend.ticket.repository.TicketRepository;
import com.example.backend.ticket.service.TicketService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TicketTest {
    @InjectMocks
    private TicketService ticketService;

    @InjectMocks
    private TicketController ticketController;

    @Mock
    private TicketRepository ticketRepositoryMock;

    @Mock
    private TicketService ticketServiceMock;

    @Test
    void ticketServiceTest() throws Exception
    {
        when(ticketRepositoryMock.findAll()).thenReturn(getTickets());
        List<Ticket> result = ticketService.getAllTickets();

        assertEquals(3, result.size());
        double[] prices = {5, 5.5, 6};
        boolean[] active = {true, false, true};

        for(int i = 0; i < result.size(); i++) {
            assertEquals(result.get(i).getPrice(), prices[i]);
            assertEquals(result.get(i).isActive(), active[i]);
        }
    }

    @Test
    void ticketControllerTest() throws Exception {
        when(ticketServiceMock.getAllTickets()).thenReturn(getTickets());
        ResponseEntity<?> response = ticketController.getTickets();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(3, ((List<?>) response.getBody()).size());
    }

    @Test
    void ticketControllerEmptyTest() throws Exception {
        when(ticketServiceMock.getAllTickets()).thenReturn(List.of());
        ResponseEntity<?> response = ticketController.getTickets();

        assertEquals(204, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    List<Ticket> getTickets()
    {
        Ticket b1 = new TicketTimeBased();
        Ticket b2 = new TicketPeriodic();
        Ticket b3 = new TicketSingleRide();

        b1.setPrice(5);
        b1.setDiscountAvailable(true);
        b1.setActive(true);

        b2.setPrice(5.5);
        b2.setDiscountAvailable(true);
        b2.setActive(false);

        b3.setPrice(6);
        b3.setDiscountAvailable(false);
        b3.setActive(true);

        return List.of(b1, b2, b3);
    }
}

package com.example.backend.dto.purchasedTicket;

import com.example.backend.model.ticket.Ticket;
import com.example.backend.model.purchasedTicket.TicketType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NewTicketDTO {
    private TicketType ticketType;
    private boolean reduced;
    private LocalDateTime startTime;
    private Ticket baseTicket;

}

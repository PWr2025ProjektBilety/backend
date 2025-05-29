package com.example.backend.purchasedticket.dto;

import com.example.backend.ticket.model.Ticket;
import com.example.backend.purchasedticket.model.TicketType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NewTicketDTO {
    private TicketType ticketType;
    private boolean reduced;
    private LocalDateTime startTime;
    private Ticket baseTicket;

}

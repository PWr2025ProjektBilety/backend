package com.example.backend.dto.purchasedTicket;

import com.example.backend.model.purchasedTicket.TicketType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BuyTicketRequestDTO {
    private TicketType ticketType;
    private Long ticketId;
    private boolean reduced;
    private LocalDateTime startTime;

}

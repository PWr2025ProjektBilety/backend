package com.example.backend.purchasedticket.dto;

import com.example.backend.purchasedticket.model.TicketType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BuyTicketRequestDTO {
    private TicketType ticketType;
    private Long ticketId;
    private boolean reduced;
    private LocalDateTime startTime;

}

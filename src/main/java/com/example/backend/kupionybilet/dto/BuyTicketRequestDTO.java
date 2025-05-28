package com.example.backend.kupionybilet.dto;

import com.example.backend.kupionybilet.model.TicketType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BuyTicketRequestDTO {
    private TicketType ticketType;
    private Long ticketId;
    private boolean reduced;
    private LocalDateTime startTime;

}

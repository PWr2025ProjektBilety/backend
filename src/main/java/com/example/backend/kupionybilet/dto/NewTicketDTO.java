package com.example.backend.kupionybilet.dto;

import com.example.backend.bilet.model.Bilet;
import com.example.backend.kupionybilet.model.TicketType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NewTicketDTO {
    private TicketType ticketType;
    private boolean reduced;
    private LocalDateTime startTime;
    private Bilet baseTicket;

}

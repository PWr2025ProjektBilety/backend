package com.example.backend.kupionybilet.dto;

import com.example.backend.kupionybilet.model.TicketType;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BuyTicketRequestDTO {
    private TicketType ticketType;
    private Long ticketId;
    private boolean isNormal;
    private LocalDateTime startTime;

    public NewTicketDTO toNewTicketDTO() {
        NewTicketDTO newTicketDTO = new NewTicketDTO();
        newTicketDTO.setTicketType(ticketType);
        newTicketDTO.setNormal(isNormal);
        newTicketDTO.setStartTime(startTime);
        return newTicketDTO;
    }
}

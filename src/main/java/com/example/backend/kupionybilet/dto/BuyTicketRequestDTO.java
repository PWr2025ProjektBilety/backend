package com.example.backend.kupionybilet.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class BuyTicketRequestDTO {
    private String ticketType;
    private boolean isNormal;
    private LocalDateTime startTime;
}

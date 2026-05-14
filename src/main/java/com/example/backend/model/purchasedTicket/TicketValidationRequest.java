package com.example.backend.model.purchasedTicket;

import lombok.Data;

@Data
public class TicketValidationRequest {
    private String ticketId;
    private String vehicleId;
}

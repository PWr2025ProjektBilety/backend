package com.example.backend.purchasedticket.model;

import lombok.Data;

@Data
public class TicketValidationRequest {
    private String ticketId;
    private String vehicleId;
}

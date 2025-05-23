package com.example.backend.kupionybilet.model;

import lombok.Data;

@Data
public class TicketValidationRequest {
    private String ticketId;
    private String vehicleId;
}

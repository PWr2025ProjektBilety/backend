package com.example.backend.dto.ticketInspection;

import lombok.Data;

@Data
public class InspectTicketRequestDTO {

    private String ticketCode;
    private String vehicleId;
}

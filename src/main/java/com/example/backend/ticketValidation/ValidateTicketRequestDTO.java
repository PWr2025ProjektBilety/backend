package com.example.backend.ticketValidation;

import lombok.Data;

@Data
public class ValidateTicketRequestDTO {

    private String ticketCode;
    private String vehicleId;
}

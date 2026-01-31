package com.example.backend.dto.ticketInspection;

import lombok.Data;

@Data
public class VehicleTicketValidationLockRequestDTO {
    private String vehicleId;
    private Integer durationMinutes;
}

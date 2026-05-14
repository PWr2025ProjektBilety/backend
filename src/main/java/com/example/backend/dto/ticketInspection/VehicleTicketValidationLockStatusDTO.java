package com.example.backend.dto.ticketInspection;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VehicleTicketValidationLockStatusDTO {
    private boolean locked;
    private Long lockedUntilEpochSeconds;
    private Long remainingSeconds;
}

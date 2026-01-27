package com.example.backend.controller.ticketInspection;

import com.example.backend.dto.ticketInspection.VehicleTicketValidationLockRequestDTO;
import com.example.backend.dto.ticketInspection.VehicleTicketValidationLockStatusDTO;
import com.example.backend.service.purchasedTicket.VehicleTicketValidationLockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Clock;
import java.time.Duration;
import java.time.Instant;

@PreAuthorize("hasRole('INSPECTOR')")
@RestController
@RequestMapping("/api/vehicle-ticket-validation-lock")
public class VehicleTicketValidationLockController {

    @Autowired
    private VehicleTicketValidationLockService lockService;

    @Autowired
    private Clock clock;

    @PostMapping("/lock")
    public ResponseEntity<VehicleTicketValidationLockStatusDTO> lock(@RequestBody VehicleTicketValidationLockRequestDTO request) {
        String vehicleId = request.getVehicleId();
        Integer mins = request.getDurationMinutes();
        Duration duration = mins == null ? null : Duration.ofMinutes(mins);

        Instant lockedUntil = lockService.lock(vehicleId, duration);
        long nowEpoch = Instant.now(clock).getEpochSecond();
        long untilEpoch = lockedUntil.getEpochSecond();
        long remaining = Math.max(0, untilEpoch - nowEpoch);

        return ResponseEntity.ok(new VehicleTicketValidationLockStatusDTO(true, untilEpoch, remaining));
    }

    @PostMapping("/unlock")
    public ResponseEntity<VehicleTicketValidationLockStatusDTO> unlock(@RequestBody VehicleTicketValidationLockRequestDTO request) {
        lockService.unlock(request.getVehicleId());
        return ResponseEntity.ok(new VehicleTicketValidationLockStatusDTO(false, null, 0L));
    }

    @GetMapping("/{vehicleId}")
    public ResponseEntity<VehicleTicketValidationLockStatusDTO> status(@PathVariable String vehicleId) {
        Instant lockedUntil = lockService.getLockedUntil(vehicleId);
        if (lockedUntil == null) {
            return ResponseEntity.ok(new VehicleTicketValidationLockStatusDTO(false, null, 0L));
        }

        long nowEpoch = Instant.now(clock).getEpochSecond();
        long untilEpoch = lockedUntil.getEpochSecond();
        long remaining = Math.max(0, untilEpoch - nowEpoch);

        return ResponseEntity.ok(new VehicleTicketValidationLockStatusDTO(true, untilEpoch, remaining));
    }
}

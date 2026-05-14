package com.example.backend.model.purchasedTicket;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "vehicle_ticket_validation_lock")
@Getter
@Setter
@NoArgsConstructor
public class VehicleTicketValidationLock {

    @Id
    @Column(name = "vehicle_id", nullable = false, length = 64)
    private String vehicleId;

    @Column(name = "locked_until", nullable = false)
    private Instant lockedUntil;

    public VehicleTicketValidationLock(String vehicleId, Instant lockedUntil) {
        this.vehicleId = vehicleId;
        this.lockedUntil = lockedUntil;
    }
}

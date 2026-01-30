package com.example.backend.repository.purchasedTicket;

import com.example.backend.model.purchasedTicket.VehicleTicketValidationLock;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleTicketValidationLockRepository extends JpaRepository<VehicleTicketValidationLock, String> {
}

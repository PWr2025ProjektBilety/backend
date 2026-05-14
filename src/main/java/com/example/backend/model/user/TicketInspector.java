package com.example.backend.model.user;

import com.example.backend.model.purchasedTicket.PurchasedTicketTimeBased;
import com.example.backend.model.purchasedTicket.PurchasedTicketSingleRide;
import com.example.backend.model.purchasedTicket.PurchasedTicketPeriodic;
import com.example.backend.visitor.TicketInspectionVisitor;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@DiscriminatorValue("inspector")
public class TicketInspector extends User implements TicketInspectionVisitor {

    @Override
    public boolean visit(PurchasedTicketSingleRide ticket, String vehicleId) {
        return ticket.isValidated() &&
                ticket.getVehicleId() != null &&
                ticket.getVehicleId().equals(vehicleId);
    }

    @Override
    public boolean visit(PurchasedTicketTimeBased ticket, String vehicleId) {
        return ticket.isValidated() &&
                ticket.getExpirationDate() != null &&
                ticket.getExpirationDate().isAfter(LocalDateTime.now());
    }

    @Override
    public boolean visit(PurchasedTicketPeriodic ticket, String vehicleId) {
        return ticket.getValidTo() != null &&
                ticket.getValidTo().isAfter(LocalDateTime.now()) &&
                ticket.getValidFrom() != null &&
                ticket.getValidFrom().isBefore(LocalDateTime.now());
    }
}


package com.example.backend.purchasedticket.model;

import com.example.backend.ticket.model.TicketSingleRide;
import com.example.backend.ticketValidation.TicketValidationVisitor;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class PurchasedTicketSingleRide extends PurchasedTicket {
    @Basic
    @Column(nullable = true)
    private String vehicleId;

    @NotNull
    @Basic
    @Column(nullable = false)
    private boolean isValidated;

    @ManyToOne(optional = false)
    private TicketSingleRide singleTicket;

    public boolean validate(String vehicleId) {
        if(isValidated || vehicleId == null || vehicleId.isEmpty()) {
            return false;
        }

        this.isValidated = true;
        this.vehicleId = vehicleId;
        return true;
    }

    @Override
    public boolean accept(TicketValidationVisitor visitor, String vehicleId) {
        return visitor.visit(this, vehicleId);
    }
}

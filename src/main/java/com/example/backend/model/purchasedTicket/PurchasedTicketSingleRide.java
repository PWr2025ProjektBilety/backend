package com.example.backend.model.purchasedTicket;

import com.example.backend.model.ticket.TicketSingleRide;
import com.example.backend.visitor.TicketInspectionVisitor;
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
        System.out.println("vehicleId: " + vehicleId);
        return true;
    }

    @Override
    public boolean accept(TicketInspectionVisitor visitor, String vehicleId) {
        return visitor.visit(this, vehicleId);
    }
}

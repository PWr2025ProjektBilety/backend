package com.example.backend.purchasedticket.model;

import com.example.backend.ticket.model.TicketTimeBased;
import com.example.backend.ticketInspection.TicketInspectionVisitor;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class PurchasedTicketTimeBased extends PurchasedTicket {
    @NotNull
    @Basic
    @Column(nullable = false)
    private boolean isValidated;

    @Column(nullable = true)
    private LocalDateTime validationDate;

    @Column(nullable = true)
    private LocalDateTime expirationDate;

    @ManyToOne(optional = false)
    private TicketTimeBased timeBasedTicket;

    public boolean validate(String vehicleId) {
        if(isValidated || vehicleId == null || vehicleId.isEmpty()) {
            return false;
        }

        LocalDateTime now = LocalDateTime.now();
        this.isValidated = true;
        this.validationDate = now;
        this.expirationDate = now.plusMinutes(timeBasedTicket.getValidityPeriod());
        return true;
    }

    @Override
    public boolean accept(TicketInspectionVisitor visitor, String vehicleId) {
        return visitor.visit(this, vehicleId);
    }

}

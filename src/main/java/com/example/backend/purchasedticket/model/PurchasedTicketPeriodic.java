package com.example.backend.purchasedticket.model;

import com.example.backend.ticket.model.TicketPeriodic;
import com.example.backend.ticketInspection.TicketInspectionVisitor;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class PurchasedTicketPeriodic extends PurchasedTicket {

    @ManyToOne(optional = false)
    private TicketPeriodic periodicTicket;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime validFrom;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime validTo;

    @Override
    public boolean accept(TicketInspectionVisitor visitor, String vehicleId) {
        return visitor.visit(this, vehicleId);
    }
}

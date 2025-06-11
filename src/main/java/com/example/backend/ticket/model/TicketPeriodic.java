package com.example.backend.ticket.model;

import com.example.backend.purchasedticket.model.PurchasedTicketPeriodic;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
public class TicketPeriodic extends Ticket {
    @NotNull
    @Basic
    @Column(nullable = false)
    private Long validityPeriod;

    @JsonIgnore
    @OneToMany(mappedBy = "periodicTicket")
    private Set<PurchasedTicketPeriodic> purchasedPeriodicTickets;
}

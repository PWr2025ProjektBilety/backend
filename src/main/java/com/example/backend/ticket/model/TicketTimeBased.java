package com.example.backend.ticket.model;

import com.example.backend.purchasedticket.model.PurchasedTicketTimeBased;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
public class TicketTimeBased extends Ticket {
    @NotNull
    @Basic
    @Column(nullable = false)
    private Long validityPeriod;

    @OneToMany(mappedBy = "timeBasedTicket")
    private Set<PurchasedTicketTimeBased> purchasedTimeBasedTickets;
}

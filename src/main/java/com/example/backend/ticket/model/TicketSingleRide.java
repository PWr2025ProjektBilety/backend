package com.example.backend.ticket.model;

import com.example.backend.purchasedticket.model.PurchasedTicketSingleRide;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
public class TicketSingleRide extends Ticket {
    @JsonIgnore
    @OneToMany(mappedBy = "singleTicket")
    private Set<PurchasedTicketSingleRide> purchasedTicketSingleRides;
}

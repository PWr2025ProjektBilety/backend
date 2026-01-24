package com.example.backend.ticket.model;

import com.example.backend.purchasedticket.model.PurchasedTicketPeriodic;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;

import java.util.Set;

@Entity
@Getter
@Setter
@DiscriminatorValue("PERIODIC_TICKET")
@SQLDelete(sql = "UPDATE ticket SET is_active = false WHERE id = ?")
public class TicketPeriodic extends Ticket {
    @NotNull
    @Basic
    private Long validityPeriod;

    @JsonIgnore
    @OneToMany(mappedBy = "periodicTicket")
    private Set<PurchasedTicketPeriodic> purchasedPeriodicTickets;
}

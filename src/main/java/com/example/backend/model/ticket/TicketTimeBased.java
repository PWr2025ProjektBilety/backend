package com.example.backend.model.ticket;

import com.example.backend.model.purchasedTicket.PurchasedTicketTimeBased;
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
@DiscriminatorValue("TIME_BASED_TICKET")
@SQLDelete(sql = "UPDATE ticket SET is_active = false WHERE id = ?")
public class TicketTimeBased extends Ticket {
    @NotNull
    @Basic
    private Long validityPeriod;

    @JsonIgnore
    @OneToMany(mappedBy = "timeBasedTicket")
    private Set<PurchasedTicketTimeBased> purchasedTimeBasedTickets;
}

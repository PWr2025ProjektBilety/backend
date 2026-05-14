package com.example.backend.model.ticket;

import com.example.backend.model.purchasedTicket.PurchasedTicketSingleRide;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;

import java.util.Set;

@Entity
@Getter
@Setter
@DiscriminatorValue("SINGLE_RIDE_TICKET")
@SQLDelete(sql = "UPDATE ticket SET is_active = false WHERE id = ?")
public class TicketSingleRide extends Ticket {
    @JsonIgnore
    @OneToMany(mappedBy = "singleTicket")
    private Set<PurchasedTicketSingleRide> purchasedTicketSingleRides;
}

package com.example.backend.user.model;

import com.example.backend.purchasedticket.model.PurchasedTicket;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@DiscriminatorValue("passenger")
public class Passenger extends User {

    @OneToMany(mappedBy = "passenger")
    private Set<PurchasedTicket> kupioneBilety;
}

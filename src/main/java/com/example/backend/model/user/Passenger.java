package com.example.backend.model.user;

import com.example.backend.model.purchasedTicket.PurchasedTicket;
import com.fasterxml.jackson.annotation.JsonIgnore;
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
    @JsonIgnore
    private Set<PurchasedTicket> kupioneBilety;
}

package com.example.backend.purchasedticket.model;

import com.example.backend.ticketValidation.TicketValidationVisitor;
import com.example.backend.user.model.Passenger;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
public class PurchasedTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Basic
    @Column(unique = true, nullable = false)
    private String code;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime purchaseDate;

    @NotNull
    @Basic
    @Column(nullable = false)
    private boolean isReduced;

    @NotNull
    @Basic
    @Column(nullable = false)
    private double finalPrice;

    @ManyToOne(optional = false)
    private Passenger passenger;

    public boolean validate(String vehicleId) {
        return false;
    }

    public boolean accept(TicketValidationVisitor visitor, String vehicleId) {
        return false;
    }


}

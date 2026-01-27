package com.example.backend.model.purchasedTicket;

import com.example.backend.visitor.TicketInspectionVisitor;
import com.example.backend.model.user.User;
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

    @ManyToOne
    private User passenger;

    public boolean validate(String vehicleId) {
        return false;
    }

    public boolean accept(TicketInspectionVisitor visitor, String vehicleId) {
        return false;
    }


}

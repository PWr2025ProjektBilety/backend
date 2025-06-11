package com.example.backend.ticket.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(value = TicketSingleRide.class, name = "SINGLE_RIDE_TICKET"),
        @JsonSubTypes.Type(value = TicketTimeBased.class, name = "TIME_BASED_TICKET"),
        @JsonSubTypes.Type(value = TicketPeriodic.class, name = "PERIODIC_TICKET")
})
@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
@SQLDelete(sql = "UPDATE bilet SET is_active = false WHERE id = ?")
@Where(clause = "is_active = true")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(nullable = false)
    @Basic
    private double price;

    @NotNull
    @Column(nullable = false)
    @Basic
    private boolean isDiscountAvailable;

    @NotNull
    @Column(nullable = false)
    @Basic
    private boolean isActive;
}

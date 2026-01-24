package com.example.backend.ticket.model;

import com.example.backend.user.model.Admin;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;

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
@DiscriminatorColumn(name = "ticket_category", discriminatorType = DiscriminatorType.STRING)
@SQLDelete(sql = "UPDATE ticket SET is_active = false WHERE id = ?")
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

    @ManyToOne(optional = false)
    private Admin admin;
}

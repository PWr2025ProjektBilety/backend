package com.example.backend.kupionybilet.model;

import com.example.backend.kupionybilet.dto.KupionyBiletDTO;
import com.example.backend.ticketValidation.TicketValidationVisitor;
import com.example.backend.uzytkownik.model.Pasazer;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
public class KupionyBilet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Basic
    @Column(unique = true, nullable = false)
    private String kod;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime dataZakupu;

    @NotNull
    @Basic
    @Column(nullable = false)
    private boolean czyUlgowy;

    @NotNull
    @Basic
    @Column(nullable = false)
    private double koncowaCena;

    @ManyToOne(optional = false)
    private Pasazer pasazer;

    public boolean validate(String vehicleId) {
        return false;
    }

    public boolean accept(TicketValidationVisitor visitor, String vehicleId) {
        return false;
    }


}

package com.example.backend.kupionybilet.model;

import com.example.backend.bilet.model.BiletOkresowy;
import com.example.backend.ticketValidation.TicketValidationVisitor;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class KupionyBiletOkresowy extends KupionyBilet {

    @ManyToOne(optional = false)
    private BiletOkresowy biletOkresowy;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime waznyOd;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime waznyDo;

    @Override
    public boolean accept(TicketValidationVisitor visitor, String vehicleId) {
        return visitor.visit(this, vehicleId);
    }
}

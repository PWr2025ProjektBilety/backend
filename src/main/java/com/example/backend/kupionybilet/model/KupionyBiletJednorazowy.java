package com.example.backend.kupionybilet.model;

import com.example.backend.bilet.model.BiletJednorazowy;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class KupionyBiletJednorazowy extends KupionyBilet {
    @Basic
    @Column(nullable = true)
    private String identyfikatorPojazu;

    @NotNull
    @Basic
    @Column(nullable = false)
    private boolean czySkasowany;

    @ManyToOne(optional = false)
    private BiletJednorazowy biletJednorazowy;

    public boolean validate(String vehicleId) {
        if(czySkasowany || vehicleId == null || vehicleId.isEmpty()) {
            return false;
        }

        this.czySkasowany = true;
        this.identyfikatorPojazu = vehicleId;
        return true;
    }
}

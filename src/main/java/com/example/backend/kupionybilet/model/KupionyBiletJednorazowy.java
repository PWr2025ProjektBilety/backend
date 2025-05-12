package com.example.backend.kupionybilet.model;

import com.example.backend.bilet.model.BiletJednorazowy;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@DiscriminatorValue("kupionyBiletJednorazowy")
public class KupionyBiletJednorazowy extends KupionyBilet {
    @NotNull
    @Basic
    @Column(nullable = false)
    private String identyfikatorPojazu;

    @NotNull
    @Basic
    @Column(nullable = false)
    private boolean czySkasowany;

    @ManyToOne(optional = false)
    private BiletJednorazowy biletJednorazowy;
}

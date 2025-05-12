package com.example.backend.kupionybilet.model;

import com.example.backend.bilet.model.BiletCzasowy;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@DiscriminatorValue("kupionyBiletCzasowy")
public class KupionyBiletCzasowy extends KupionyBilet {
    @NotNull
    @Basic
    @Column(nullable = false)
    private boolean czySkasowany;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime dataSkasowania;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime dataWaznosci;

    @ManyToOne(optional = false)
    private BiletCzasowy biletCzasowy;
}

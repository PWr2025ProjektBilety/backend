package com.example.backend.bilet.model;

import com.example.backend.kupionybilet.model.KupionyBiletCzasowy;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@DiscriminatorValue("biletCzasowy")
public class BiletCzasowy extends Bilet {
    @NotNull
    @Basic
    @Column(nullable = false)
    private Long okresWaznosci;

    @OneToMany(mappedBy = "biletCzasowy")
    private Set<KupionyBiletCzasowy> kupioneBiletyCzasowe;
}

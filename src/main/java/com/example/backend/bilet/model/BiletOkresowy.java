package com.example.backend.bilet.model;

import com.example.backend.kupionybilet.model.KupionyBiletOkresowy;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@DiscriminatorValue("biletOkresowy")
public class BiletOkresowy extends Bilet {
    @NotNull
    @Basic
    @Column(nullable = false)
    private Long okresWaznosci;

    @OneToMany(mappedBy = "biletOkresowy")
    private Set<KupionyBiletOkresowy> kupioneBiletyOkresowe;
}

package com.example.backend.bilet.model;

import com.example.backend.kupionybilet.model.KupionyBiletJednorazowy;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
public class BiletJednorazowy extends Bilet {
    @OneToMany(mappedBy = "biletJednorazowy")
    private Set<KupionyBiletJednorazowy> kupioneBiletyJednorazowe;
}

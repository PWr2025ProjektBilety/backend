package com.example.backend.uzytkownik.model;

import com.example.backend.kupionybilet.model.KupionyBilet;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@DiscriminatorValue("pasazer")
public class Pasazer extends Uzytkownik {

    @OneToMany(mappedBy = "pasazer")
    private Set<KupionyBilet> kupioneBilety;
}

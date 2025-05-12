package com.example.backend.kupionybilet.model;

import com.example.backend.bilet.model.BiletOkresowy;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@DiscriminatorValue("kupionyBiletOkresowy")
public class KupionyBiletOkresowy {

    @ManyToOne(optional = false)
    private BiletOkresowy biletOkresowy;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime waznyOd;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime waznyDo;
}

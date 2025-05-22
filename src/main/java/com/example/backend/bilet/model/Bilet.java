package com.example.backend.bilet.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
@SQLDelete(sql = "UPDATE bilet SET is_active = false WHERE id = ?")
@Where(clause = "is_active = true")
public class Bilet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(nullable = false)
    @Basic
    private double cena;

    @NotNull
    @Column(nullable = false)
    @Basic
    private boolean czyMozliwaUlga;

    @NotNull
    @Column(nullable = false)
    @Basic
    private boolean isActive;
}

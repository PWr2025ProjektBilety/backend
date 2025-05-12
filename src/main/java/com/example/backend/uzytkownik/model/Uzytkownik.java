package com.example.backend.uzytkownik.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name="type",
    discriminatorType = DiscriminatorType.STRING)
public class Uzytkownik {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @NotNull
    @Column(nullable = false)
    @Basic
    private String login;

    @NotNull
    @Column(nullable = false)
    @Basic
    private String haslo;

    @NotNull
    @Column(nullable = false)
    @Basic
    private String role;
}

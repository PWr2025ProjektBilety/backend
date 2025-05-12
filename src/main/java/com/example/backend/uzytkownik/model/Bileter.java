package com.example.backend.uzytkownik.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@DiscriminatorValue("bileter")
public class Bileter extends Uzytkownik {

}

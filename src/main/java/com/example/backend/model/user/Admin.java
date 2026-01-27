package com.example.backend.model.user;

import com.example.backend.model.ticket.Ticket;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@DiscriminatorValue("admin")
public class Admin extends User {
    @OneToMany(mappedBy = "admin")
    @JsonIgnore
    private Set<Ticket> bilety;
}

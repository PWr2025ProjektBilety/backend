package com.example.backend.bonus.model;

import com.example.backend.user.model.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "bonus")
public class Bonus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(nullable = false)
    private Integer points = 0;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}

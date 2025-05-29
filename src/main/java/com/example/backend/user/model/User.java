package com.example.backend.user.model;

import com.example.backend.user.dto.UserDTO;
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
@Table(name = "`user`")
public class User {
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
    private String password;

    @NotNull
    @Column(nullable = false)
    @Basic
    private String role;

    public UserDTO toDTO() {
        UserDTO userDTO = new UserDTO();
        userDTO.setId(this.id);
        userDTO.setLogin(this.login);
        return userDTO;
    }
}

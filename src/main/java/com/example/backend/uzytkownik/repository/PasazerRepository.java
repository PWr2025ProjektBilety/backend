package com.example.backend.uzytkownik.repository;

import com.example.backend.uzytkownik.model.Pasazer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasazerRepository extends JpaRepository<Pasazer, Long> {

    Optional<Pasazer> findByLogin(String login);

}

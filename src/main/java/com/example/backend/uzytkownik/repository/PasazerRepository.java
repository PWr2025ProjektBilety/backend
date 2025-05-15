package com.example.backend.uzytkownik.repository;

import com.example.backend.uzytkownik.model.Pasazer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PasazerRepository extends JpaRepository<Pasazer, Long> {

}

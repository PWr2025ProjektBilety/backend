package com.example.backend.uzytkownik.repository;

import com.example.backend.uzytkownik.model.Bileter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BileterRepository extends JpaRepository<Bileter, Long> {

    Optional<Bileter> findByLogin(String login);
}

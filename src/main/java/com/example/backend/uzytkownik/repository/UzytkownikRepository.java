package com.example.backend.uzytkownik.repository;

import com.example.backend.uzytkownik.model.Uzytkownik;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UzytkownikRepository extends JpaRepository<Uzytkownik, Long> {
    boolean existsByLogin(String login);

    Optional<Uzytkownik> findByLogin(String login);
}

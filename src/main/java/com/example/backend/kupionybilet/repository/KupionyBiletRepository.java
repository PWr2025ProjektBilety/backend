package com.example.backend.kupionybilet.repository;

import com.example.backend.kupionybilet.model.KupionyBilet;
import com.example.backend.uzytkownik.model.Pasazer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KupionyBiletRepository extends JpaRepository<KupionyBilet, Long> {
    Optional<KupionyBilet> findByKod(String kod);
    Page<KupionyBilet> findAllByPasazer(Pasazer pasazer, Pageable pageable);
}

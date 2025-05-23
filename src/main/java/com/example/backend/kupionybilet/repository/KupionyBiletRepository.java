package com.example.backend.kupionybilet.repository;

import com.example.backend.kupionybilet.model.KupionyBilet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface KupionyBiletRepository extends JpaRepository<KupionyBilet, Long> {
    Optional<KupionyBilet> findByKod(String kod);
}

package com.example.backend.bilet.repository;

import com.example.backend.bilet.model.Bilet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BiletRepository extends JpaRepository<Bilet, Long> {

}

package com.example.backend.bilet.service;

import com.example.backend.bilet.model.Bilet;
import com.example.backend.bilet.repository.BiletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BiletService {
    @Autowired
    private BiletRepository biletRepository;

    public List<Bilet> getAllTickets() {
        return biletRepository.findAll();
    }
}

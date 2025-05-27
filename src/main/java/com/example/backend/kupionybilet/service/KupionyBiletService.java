package com.example.backend.kupionybilet.service;

import com.example.backend.kupionybilet.model.KupionyBilet;
import com.example.backend.kupionybilet.model.TicketValidationRequest;
import com.example.backend.kupionybilet.repository.KupionyBiletRepository;
import com.example.backend.uzytkownik.model.Pasazer;
import com.example.backend.uzytkownik.repository.PasazerRepository;
import com.example.backend.uzytkownik.repository.UzytkownikRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class KupionyBiletService {
    @Autowired
    KupionyBiletRepository kupionyBiletRepository;

    @Autowired
    PasazerRepository pasazerRepository;

    public boolean validateTicket(TicketValidationRequest ticketValidationRequest) {
        Optional<KupionyBilet> ticket = kupionyBiletRepository.findByKod(ticketValidationRequest.getTicketId());
        if(ticket.isEmpty()) {
            return false;
        }
        return ticket.get().validate(ticketValidationRequest.getVehicleId());
    }

    public KupionyBilet buyTicket(KupionyBilet kupionyBilet) {
        return kupionyBiletRepository.save(kupionyBilet);
    }

    public List<KupionyBilet> getTicketHistory(String username) {
        Pasazer pasazer = pasazerRepository.findByLogin(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        return kupionyBiletRepository.findByKod(username).stream().toList();
    }
}

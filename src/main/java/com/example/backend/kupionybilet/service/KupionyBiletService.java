package com.example.backend.kupionybilet.service;

import com.example.backend.kupionybilet.model.KupionyBilet;
import com.example.backend.kupionybilet.model.TicketValidationRequest;
import com.example.backend.kupionybilet.repository.KupionyBiletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class KupionyBiletService {
    @Autowired
    KupionyBiletRepository kupionyBiletRepository;

    public boolean validateTicket(TicketValidationRequest ticketValidationRequest) {
        Optional<KupionyBilet> ticket = kupionyBiletRepository.findByKod(ticketValidationRequest.getTicketId());
        if(ticket.isEmpty()) {
            return false;
        }
        return ticket.get().validate(ticketValidationRequest.getVehicleId());
    }
}

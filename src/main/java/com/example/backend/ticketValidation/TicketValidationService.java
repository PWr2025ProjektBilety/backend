package com.example.backend.ticketValidation;

import com.example.backend.bilet.repository.BiletRepository;
import com.example.backend.kupionybilet.model.KupionyBilet;
import com.example.backend.kupionybilet.repository.KupionyBiletRepository;
import com.example.backend.uzytkownik.model.Bileter;
import com.example.backend.uzytkownik.repository.BileterRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TicketValidationService {

    @Autowired
    KupionyBiletRepository kupionyBiletRepository;

    @Autowired
    BileterRepository bileterRepository;


    public boolean validateTicket(ValidateTicketRequestDTO dto, String ticketInspectorusername){

        KupionyBilet kupionyBilet = kupionyBiletRepository.findByKod(dto.getTicketCode()).orElseThrow(
                () -> new RuntimeException("Ticket not found with ID: " + dto.getTicketCode())
        );

        Bileter ticketInspector = bileterRepository.findByLogin(ticketInspectorusername).orElseThrow(
                () -> new RuntimeException("Ticket inspector not found with username: " + ticketInspectorusername)
        );

        return kupionyBilet.accept(ticketInspector, dto.getVehicleId());

    }

}

package com.example.backend.kupionybilet.service;

import com.example.backend.bilet.model.Bilet;
import com.example.backend.bilet.repository.BiletRepository;
import com.example.backend.kupionybilet.dto.BuyTicketRequestDTO;
import com.example.backend.kupionybilet.dto.KupionyBiletDTO;
import com.example.backend.kupionybilet.dto.NewTicketDTO;
import com.example.backend.kupionybilet.mapper.BuyTicketRequestMapper;
import com.example.backend.kupionybilet.mapper.KupionyBiletMapper;
import com.example.backend.kupionybilet.model.KupionyBilet;
import com.example.backend.kupionybilet.model.KupionyBiletFactory;
import com.example.backend.kupionybilet.model.TicketValidationRequest;
import com.example.backend.kupionybilet.repository.KupionyBiletRepository;
import com.example.backend.uzytkownik.model.Pasazer;
import com.example.backend.uzytkownik.repository.PasazerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class KupionyBiletService {
    @Autowired
    KupionyBiletRepository kupionyBiletRepository;

    @Autowired
    PasazerRepository pasazerRepository;

    @Autowired
    BiletRepository biletRepository;

    @Autowired
    KupionyBiletMapper kupionyBiletMapper;

    @Autowired
    BuyTicketRequestMapper buyTicketRequestMapper;


    public boolean validateTicket(TicketValidationRequest ticketValidationRequest) {
        Optional<KupionyBilet> ticket = kupionyBiletRepository.findByKod(ticketValidationRequest.getTicketId());
        if(ticket.isEmpty()) {
            return false;
        }
        return ticket.get().validate(ticketValidationRequest.getVehicleId());
    }

    public KupionyBiletDTO buyTicket(BuyTicketRequestDTO dto, String username) {

        Pasazer pasazer = pasazerRepository.findByLogin(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        Bilet baseTicket = biletRepository.findById(dto.getTicketId())
                .orElseThrow(() -> new RuntimeException("Ticket not found with ID: " + dto.getTicketId()));

        NewTicketDTO newTicketDTO = buyTicketRequestMapper.toNewTicketDTO(dto);
        newTicketDTO.setBaseTicket(baseTicket);

        KupionyBilet ticket = KupionyBiletFactory.createKupionyBilet(newTicketDTO);
        ticket.setPasazer(pasazer);

        return kupionyBiletMapper.toDto(kupionyBiletRepository.save(ticket));

    }



    public Page<KupionyBiletDTO> getTicketHistory(String username, Pageable pageable) {
        Pasazer pasazer = pasazerRepository.findByLogin(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        return kupionyBiletRepository.findAllByPasazer(pasazer, pageable).map(KupionyBilet::toDTO);
    }
}

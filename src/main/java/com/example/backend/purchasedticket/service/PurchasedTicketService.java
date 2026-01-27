package com.example.backend.purchasedticket.service;

import com.example.backend.bonus.service.BonusService;
import com.example.backend.ticket.model.Ticket;
import com.example.backend.ticket.repository.TicketRepository;
import com.example.backend.purchasedticket.dto.BuyTicketRequestDTO;
import com.example.backend.purchasedticket.dto.PurchasedTicketDTO;
import com.example.backend.purchasedticket.dto.NewTicketDTO;
import com.example.backend.purchasedticket.mapper.BuyTicketRequestMapper;
import com.example.backend.purchasedticket.mapper.PurchasedTicketMapper;
import com.example.backend.purchasedticket.model.PurchasedTicket;
import com.example.backend.purchasedticket.model.PurchasedTicketFactory;
import com.example.backend.purchasedticket.model.TicketValidationRequest;
import com.example.backend.purchasedticket.repository.PurchasedTicketRepository;
import com.example.backend.qr.QrPayloadService;
import com.example.backend.user.model.Passenger;
import com.example.backend.user.repository.PassengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PurchasedTicketService {
    @Autowired
    PurchasedTicketRepository purchasedTicketRepository;

    @Autowired
    PassengerRepository passengerRepository;

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    PurchasedTicketMapper purchasedTicketMapper;

    @Autowired
    BuyTicketRequestMapper buyTicketRequestMapper;

    @Autowired
    PurchasedTicketCodeGenerator purchasedTicketCodeGenerator;

    @Autowired
    QrPayloadService qrPayloadService;

    @Autowired
    BonusService bonusService;


    public boolean validateTicket(TicketValidationRequest ticketValidationRequest) {
        Optional<PurchasedTicket> ticket = purchasedTicketRepository.findByCode(ticketValidationRequest.getTicketId());
        if(ticket.isEmpty()) {
            return false;
        }
        boolean result = ticket.get().validate(ticketValidationRequest.getVehicleId());
        purchasedTicketRepository.save(ticket.get());
        return result;
    }

    public PurchasedTicketDTO buyTicket(BuyTicketRequestDTO dto, String username) {

        Passenger passenger = passengerRepository.findByLogin(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        Ticket baseTicket = ticketRepository.findById(dto.getTicketId())
                .orElseThrow(() -> new RuntimeException("Ticket not found with ID: " + dto.getTicketId()));

        NewTicketDTO newTicketDTO = buyTicketRequestMapper.toNewTicketDTO(dto);
        newTicketDTO.setBaseTicket(baseTicket);

        try {
            PurchasedTicket ticket = PurchasedTicketFactory.createPurchasedTicket(newTicketDTO);
            ticket.setPassenger(passenger);
            ticket.setCode(purchasedTicketCodeGenerator.generateCode());

            PurchasedTicket savedTicket = purchasedTicketRepository.save(ticket);
            bonusService.addPoints(passenger, savedTicket.getFinalPrice());

            PurchasedTicketDTO saved = purchasedTicketMapper.toDto(savedTicket);
            saved.setQrPayload(qrPayloadService.createPayload(saved.getCode()));
            return saved;
        }
        catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid ticket data provided: " + e.getMessage());
        }

    }



    public Page<PurchasedTicketDTO> getTicketHistory(String username, Pageable pageable) {
        Passenger passenger = passengerRepository.findByLogin(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        return purchasedTicketRepository.findAllByPassenger(passenger, pageable)
                .map(purchasedTicketMapper::toDto)
                .map(dto -> {
                    dto.setQrPayload(qrPayloadService.createPayload(dto.getCode()));
                    return dto;
                });
    }
}

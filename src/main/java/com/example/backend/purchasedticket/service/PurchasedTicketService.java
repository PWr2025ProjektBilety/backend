package com.example.backend.purchasedticket.service;

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

        PurchasedTicket ticket = PurchasedTicketFactory.createPurchasedTicket(newTicketDTO);
        ticket.setPassenger(passenger);
        ticket.setCode(purchasedTicketCodeGenerator.generateCode());

        return purchasedTicketMapper.toDto(purchasedTicketRepository.save(ticket));

    }



    public Page<PurchasedTicketDTO> getTicketHistory(String username, Pageable pageable) {
        Passenger passenger = passengerRepository.findByLogin(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));
        return purchasedTicketRepository.findAllByPassenger(passenger, pageable).map(purchasedTicketMapper::toDto);
    }
}

package com.example.backend.ticketValidation;

import com.example.backend.purchasedticket.model.PurchasedTicket;
import com.example.backend.purchasedticket.repository.PurchasedTicketRepository;
import com.example.backend.user.model.TicketInspector;
import com.example.backend.user.repository.InspectorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TicketValidationService {

    @Autowired
    PurchasedTicketRepository purchasedTicketRepository;

    @Autowired
    InspectorRepository inspectorRepository;


    public boolean validateTicket(ValidateTicketRequestDTO dto, String ticketInspectorusername){

        PurchasedTicket purchasedTicket = purchasedTicketRepository.findByCode(dto.getTicketCode()).orElseThrow(
                () -> new RuntimeException("Ticket not found with ID: " + dto.getTicketCode())
        );

        TicketInspector ticketInspector = inspectorRepository.findByLogin(ticketInspectorusername).orElseThrow(
                () -> new RuntimeException("Ticket inspector not found with username: " + ticketInspectorusername)
        );

        return purchasedTicket.accept(ticketInspector, dto.getVehicleId());

    }

}

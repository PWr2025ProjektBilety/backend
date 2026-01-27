package com.example.backend.service.ticketInspection;

import com.example.backend.dto.ticketInspection.InspectTicketBodyDTO;
import com.example.backend.dto.ticketInspection.InspectTicketRequestDTO;
import com.example.backend.dto.ticketInspection.InspectTicketResponseDTO;
import com.example.backend.model.purchasedTicket.PurchasedTicket;
import com.example.backend.model.purchasedTicket.PurchasedTicketPeriodic;
import com.example.backend.model.purchasedTicket.PurchasedTicketSingleRide;
import com.example.backend.model.purchasedTicket.PurchasedTicketTimeBased;
import com.example.backend.repository.purchasedTicket.PurchasedTicketRepository;
import com.example.backend.model.user.TicketInspector;
import com.example.backend.repository.user.InspectorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TicketInspectionService {

    @Autowired
    PurchasedTicketRepository purchasedTicketRepository;

    @Autowired
    InspectorRepository inspectorRepository;

    @Autowired
    QrPayloadService qrPayloadService;


    public boolean validateTicket(InspectTicketRequestDTO dto, String ticketInspectorusername){

        PurchasedTicket purchasedTicket = purchasedTicketRepository.findByCode(dto.getTicketCode()).orElseThrow(
                () -> new RuntimeException("Ticket not found with ID: " + dto.getTicketCode())
        );

        TicketInspector ticketInspector = inspectorRepository.findByLogin(ticketInspectorusername).orElseThrow(
                () -> new RuntimeException("Ticket inspector not found with username: " + ticketInspectorusername)
        );

        return purchasedTicket.accept(ticketInspector, dto.getVehicleId());

    }

    public InspectTicketResponseDTO inspectTicket(String code, InspectTicketBodyDTO request, String ticketInspectorUsername) {

        String extractedCode = qrPayloadService.extractTicketCodeOrNull(code);
        if (extractedCode == null) {
            return InspectTicketResponseDTO.invalid("invalid-qr");
        }

        inspectorRepository.findByLogin(ticketInspectorUsername).orElseThrow(
                () -> new RuntimeException("Ticket inspector not found with username: " + ticketInspectorUsername)
        );

        PurchasedTicket purchasedTicket = purchasedTicketRepository.findByCode(extractedCode).orElse(null);
        if (purchasedTicket == null) {
            return InspectTicketResponseDTO.invalid("ticket-not-found");
        }

        String vehicleId = request.getVehicleId() == null ? "" : request.getVehicleId().trim();
        LocalDateTime now = LocalDateTime.now();

        if (purchasedTicket instanceof PurchasedTicketSingleRide singleRide) {
            if (!singleRide.isValidated()) {
                return InspectTicketResponseDTO.invalid("ticket-not-validated");
            }

            if (singleRide.getVehicleId() == null || !singleRide.getVehicleId().equals(vehicleId)) {
                return InspectTicketResponseDTO.invalid("ticket-not-valid-for-vehicle");
            }

            return InspectTicketResponseDTO.valid();
        }

        if (purchasedTicket instanceof PurchasedTicketTimeBased timeBased) {
            if (!timeBased.isValidated()) {
                return InspectTicketResponseDTO.invalid("ticket-not-validated");
            }

            if (timeBased.getExpirationDate() == null || !timeBased.getExpirationDate().isAfter(now)) {
                return InspectTicketResponseDTO.invalid("ticket-expired");
            }

            return InspectTicketResponseDTO.valid();
        }

        if (purchasedTicket instanceof PurchasedTicketPeriodic periodic) {
            if (periodic.getValidFrom() == null
                    || periodic.getValidTo() == null
                    || periodic.getValidFrom().isAfter(now)
                    || periodic.getValidTo().isBefore(now)) {
                return InspectTicketResponseDTO.invalid("ticket-expired");
            }

            return InspectTicketResponseDTO.valid();
        }

        return InspectTicketResponseDTO.invalid("ticket-not-found");
    }

}

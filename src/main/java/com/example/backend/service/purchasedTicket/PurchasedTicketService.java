package com.example.backend.service.purchasedTicket;

import com.example.backend.service.bonus.BonusService;
import com.example.backend.model.ticket.Ticket;
import com.example.backend.model.ticket.TicketSingleRide;
import com.example.backend.model.ticket.TicketTimeBased;
import com.example.backend.model.ticket.TicketPeriodic;
import com.example.backend.repository.ticket.TicketRepository;
import com.example.backend.dto.purchasedTicket.BuyTicketRequestDTO;
import com.example.backend.dto.purchasedTicket.PurchasedTicketDTO;
import com.example.backend.dto.purchasedTicket.NewTicketDTO;
import com.example.backend.mapper.purchasedTicket.BuyTicketRequestMapper;
import com.example.backend.mapper.purchasedTicket.PurchasedTicketMapper;
import com.example.backend.model.purchasedTicket.PurchasedTicket;
import com.example.backend.model.purchasedTicket.PurchasedTicketFactory;
import com.example.backend.model.purchasedTicket.TicketType;
import com.example.backend.model.purchasedTicket.TicketValidationRequest;
import com.example.backend.repository.purchasedTicket.PurchasedTicketRepository;
import com.example.backend.service.ticketInspection.QrPayloadService;
import com.example.backend.model.user.Passenger;
import com.example.backend.repository.user.PassengerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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

    @Autowired
    VehicleTicketValidationLockService vehicleTicketValidationLockService;


    public boolean validateTicket(TicketValidationRequest ticketValidationRequest) {
        if (vehicleTicketValidationLockService.isLocked(ticketValidationRequest.getVehicleId())) {
            return false;
        }
        Optional<PurchasedTicket> ticket = purchasedTicketRepository.findByCode(ticketValidationRequest.getTicketId());
        if(ticket.isEmpty()) {
            return false;
        }
        boolean result = ticket.get().validate(ticketValidationRequest.getVehicleId());
        purchasedTicketRepository.save(ticket.get());
        return result;
    }

    @Transactional
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

    @Transactional
    public PurchasedTicketDTO buyTicketWithPoints(Long ticketId, String userLogin, boolean discounted) {
        Passenger passenger = passengerRepository.findByLogin(userLogin)
                .orElseThrow(() -> new RuntimeException("User not found: " + userLogin));

        // Pobierz bilet z repozytorium
        Ticket baseTicket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket not found with ID: " + ticketId));

        // Oblicz koszt: jeśli discounted, cena/2 * 100, jeśli nie, cena * 100
        BigDecimal ticketPrice = new BigDecimal(baseTicket.getPrice());
        if (discounted) {
            ticketPrice = ticketPrice.divide(new BigDecimal(2), 2, BigDecimal.ROUND_HALF_UP);
        }
        BigDecimal costInPoints = ticketPrice.multiply(new BigDecimal(100));
        int requiredPoints = costInPoints.intValue();

        // Odejmij punkty - ta operacja rzuci RuntimeException jeśli nie ma wystarczających punktów
        bonusService.deductPoints(passenger, requiredPoints);

        // Stwórz nowy bilet w bazie z odpowiednią flagą reduced
        try {
            NewTicketDTO newTicketDTO = new NewTicketDTO();
            newTicketDTO.setBaseTicket(baseTicket);
            newTicketDTO.setReduced(discounted);

            // Określ typ biletu na podstawie instancji
            if (baseTicket instanceof TicketSingleRide) {
                newTicketDTO.setTicketType(TicketType.SINGLE_RIDE_TICKET);
            } else if (baseTicket instanceof TicketTimeBased) {
                newTicketDTO.setTicketType(TicketType.TIME_BASED_TICKET);
            } else if (baseTicket instanceof TicketPeriodic) {
                newTicketDTO.setTicketType(TicketType.PERIODIC_TICKET);
                // Dla biletów okresowych ustaw czas startu
                newTicketDTO.setStartTime(java.time.LocalDateTime.now());
            }

            PurchasedTicket ticket = PurchasedTicketFactory.createPurchasedTicket(newTicketDTO);
            ticket.setPassenger(passenger);
            ticket.setCode(purchasedTicketCodeGenerator.generateCode());

            PurchasedTicket savedTicket = purchasedTicketRepository.save(ticket);

            PurchasedTicketDTO saved = purchasedTicketMapper.toDto(savedTicket);
            saved.setQrPayload(qrPayloadService.createPayload(saved.getCode()));
            return saved;
        }
        catch (IllegalArgumentException e) {
            // Jeśli zapis biletu się nie powiedzie, to @Transactional cofnie odejmowanie punktów
            throw new RuntimeException("Failed to create ticket: " + e.getMessage());
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

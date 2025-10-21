package com.example.backend.purchasedticket.model;

import com.example.backend.ticket.model.TicketTimeBased;
import com.example.backend.ticket.model.TicketSingleRide;
import com.example.backend.ticket.model.TicketPeriodic;
import com.example.backend.purchasedticket.dto.NewTicketDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PurchasedTicketFactory {

    private static final int DISCOUNT_PERCENTAGE = 50;

    public static PurchasedTicket createPurchasedTicket(NewTicketDTO dto) {

        if (dto == null || dto.getBaseTicket() == null || dto.getTicketType() == null) {
            throw new IllegalArgumentException("Invalid ticket data provided");
        }

        PurchasedTicket ticket = null;

        switch (dto.getTicketType()) {
            case SINGLE_RIDE_TICKET:
                PurchasedTicketSingleRide singleRide = new PurchasedTicketSingleRide();
                singleRide.setValidated(false);
                TicketSingleRide biletJednorazowy = (TicketSingleRide) dto.getBaseTicket();
                singleRide.setSingleTicket(biletJednorazowy);
                ticket = singleRide;
                break;

            case PERIODIC_TICKET:
                PurchasedTicketPeriodic periodic = new PurchasedTicketPeriodic();
                periodic.setValidFrom(dto.getStartTime());
                TicketPeriodic biletOkresowy = (TicketPeriodic) dto.getBaseTicket();
                periodic.setValidTo(dto.getStartTime().plusDays(biletOkresowy.getValidityPeriod()));
                periodic.setPeriodicTicket(biletOkresowy);
                ticket = periodic;
                break;

            case TIME_BASED_TICKET:
                PurchasedTicketTimeBased timeBased = new PurchasedTicketTimeBased();
                timeBased.setValidated(false);
                TicketTimeBased biletCzasowy = (TicketTimeBased) dto.getBaseTicket();
                timeBased.setTimeBasedTicket(biletCzasowy);
                ticket = timeBased;
                break;

        }
        initializeBaseFields(ticket, dto);
        return ticket;

    }

    private static void initializeBaseFields(PurchasedTicket ticket, NewTicketDTO dto) {
        if(ticket != null) {
            ticket.setPurchaseDate(LocalDateTime.now());
            ticket.setReduced(dto.isReduced());
            ticket.setFinalPrice(dto.isReduced() ?
                dto.getBaseTicket().getPrice() * (1 - (DISCOUNT_PERCENTAGE / 100.0)) :
                    dto.getBaseTicket().getPrice());
        }
    }
}

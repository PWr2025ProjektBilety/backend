package com.example.backend.purchasedticket;

import com.example.backend.ticket.model.TicketSingleRide;
import com.example.backend.ticket.model.TicketPeriodic;
import com.example.backend.ticket.repository.TicketRepository;
import com.example.backend.purchasedticket.model.PurchasedTicket;
import com.example.backend.purchasedticket.model.PurchasedTicketSingleRide;
import com.example.backend.purchasedticket.model.PurchasedTicketPeriodic;
import com.example.backend.purchasedticket.model.TicketValidationRequest;
import com.example.backend.purchasedticket.repository.PurchasedTicketRepository;
import com.example.backend.purchasedticket.service.PurchasedTicketService;
import com.example.backend.user.model.Passenger;
import com.example.backend.user.repository.PassengerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TicketValidationTest {
    @Mock
    private PurchasedTicketRepository purchasedTicketRepositoryMock;

    @InjectMocks
    private PurchasedTicketService purchasedTicketService;

    @Test
    void serviceTestNoValidatedTicket() throws Exception {
        when(purchasedTicketRepositoryMock.findByCode("1234abcd")).thenReturn(getBoughtTicket(false));
        TicketValidationRequest ticketValidationRequest = new TicketValidationRequest();
        ticketValidationRequest.setTicketId("1234abcd");
        ticketValidationRequest.setVehicleId("7654");

        boolean result = purchasedTicketService.validateTicket(ticketValidationRequest);
        assertTrue(result);
    }

    @Test
    void serviceTestValidatedTicket() throws Exception {
        when(purchasedTicketRepositoryMock.findByCode("1234abcd")).thenReturn(getBoughtTicket(true));
        TicketValidationRequest ticketValidationRequest = new TicketValidationRequest();
        ticketValidationRequest.setTicketId("1234abcd");
        ticketValidationRequest.setVehicleId("7654");

        boolean result = purchasedTicketService.validateTicket(ticketValidationRequest);
        assertFalse(result);
    }

    @Test
    void serviceTestNoTicket() throws Exception {
        when(purchasedTicketRepositoryMock.findByCode("1234abcd")).thenReturn(Optional.empty());
        TicketValidationRequest ticketValidationRequest = new TicketValidationRequest();
        ticketValidationRequest.setTicketId("1234abcd");
        ticketValidationRequest.setVehicleId("7654");

        boolean result = purchasedTicketService.validateTicket(ticketValidationRequest);
        assertFalse(result);
    }

    @Test
    void serviceTestOkresowyTicket() throws Exception {
        when(purchasedTicketRepositoryMock.findByCode("1234abcd")).thenReturn(getBoughtOkresowyTicket());
        TicketValidationRequest ticketValidationRequest = new TicketValidationRequest();
        ticketValidationRequest.setTicketId("1234abcd");
        ticketValidationRequest.setVehicleId("7654");

        boolean result = purchasedTicketService.validateTicket(ticketValidationRequest);
        assertFalse(result);
    }

    private Optional<PurchasedTicket> getBoughtTicket(boolean isAlreadyValidated) {
        PurchasedTicketSingleRide kupionyBiletJednorazowy = new PurchasedTicketSingleRide();
        kupionyBiletJednorazowy.setCode("1234abcd");
        kupionyBiletJednorazowy.setPurchaseDate(LocalDateTime.now());
        kupionyBiletJednorazowy.setReduced(false);
        kupionyBiletJednorazowy.setFinalPrice(12);
        kupionyBiletJednorazowy.setValidated(isAlreadyValidated);
        kupionyBiletJednorazowy.setPassenger(getPasazer());
        kupionyBiletJednorazowy.setSingleTicket(getBiletJednorazowy());

        return Optional.of(kupionyBiletJednorazowy);
    }

    private Optional<PurchasedTicket> getBoughtOkresowyTicket() {
        PurchasedTicketPeriodic kupionyBiletOkresowy = new PurchasedTicketPeriodic();
        kupionyBiletOkresowy.setCode("1234abcd");
        kupionyBiletOkresowy.setPurchaseDate(LocalDateTime.now());
        kupionyBiletOkresowy.setReduced(false);
        kupionyBiletOkresowy.setFinalPrice(12);
        kupionyBiletOkresowy.setPassenger(getPasazer());
        kupionyBiletOkresowy.setPeriodicTicket(getBiletOkresowy());

        return Optional.of(kupionyBiletOkresowy);
    }

    private Passenger getPasazer() {
        Passenger p = new Passenger();
        p.setLogin("432gfds");
        p.setPassword("ewvt5ev5ry");
        p.setRole("ROLE_USER");
        return p;
    }

    private TicketSingleRide getBiletJednorazowy() {
        TicketSingleRide bilet = new TicketSingleRide();
        bilet.setPrice(1);
        bilet.setDiscountAvailable(true);
        bilet.setActive(true);
        return bilet;
    }

    private TicketPeriodic getBiletOkresowy() {
        TicketPeriodic bilet = new TicketPeriodic();
        bilet.setPrice(1);
        bilet.setDiscountAvailable(true);
        bilet.setActive(true);
        bilet.setValidityPeriod(30L);
        return bilet;
    }
}

@SpringBootTest
class TicketValidationRepoTest {
    @Autowired
    private PurchasedTicketRepository purchasedTicketRepository;

    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Test
    void test() throws Exception {
        Passenger passenger = passengerRepository.save(getPasazer());
        PurchasedTicketSingleRide jednorazowy = getBoughtTicket();
        PurchasedTicketPeriodic okresowy = getBoughtOkresowyTicket();
        jednorazowy.setPassenger(passenger);
        okresowy.setPassenger(passenger);

        purchasedTicketRepository.save(jednorazowy);
        purchasedTicketRepository.save(okresowy);

        Optional<PurchasedTicket> kupionyBilet = purchasedTicketRepository.findByCode("1234abcd");
        Optional<PurchasedTicket> kupionyBilet1 = purchasedTicketRepository.findByCode("1234");

        assertFalse(kupionyBilet.isEmpty());
        assertTrue(kupionyBilet1.isEmpty());
        assertEquals("1234abcd", kupionyBilet.get().getCode());
        assertFalse(kupionyBilet.get().isReduced());
    }

    private PurchasedTicketSingleRide getBoughtTicket() {
        PurchasedTicketSingleRide kupionyBiletJednorazowy = new PurchasedTicketSingleRide();
        kupionyBiletJednorazowy.setCode("1234abcd");
        kupionyBiletJednorazowy.setPurchaseDate(LocalDateTime.now());
        kupionyBiletJednorazowy.setReduced(false);
        kupionyBiletJednorazowy.setFinalPrice(12);
        kupionyBiletJednorazowy.setValidated(false);
        kupionyBiletJednorazowy.setSingleTicket(ticketRepository.save(getBiletJednorazowy()));

        return kupionyBiletJednorazowy;
    }

    private PurchasedTicketPeriodic getBoughtOkresowyTicket() {
        PurchasedTicketPeriodic kupionyBiletOkresowy = new PurchasedTicketPeriodic();
        kupionyBiletOkresowy.setCode("1234abce");
        kupionyBiletOkresowy.setPurchaseDate(LocalDateTime.now());
        kupionyBiletOkresowy.setReduced(false);
        kupionyBiletOkresowy.setFinalPrice(12);
        kupionyBiletOkresowy.setPeriodicTicket(ticketRepository.save(getBiletOkresowy()));
        kupionyBiletOkresowy.setValidFrom(LocalDateTime.now());
        kupionyBiletOkresowy.setValidTo(LocalDateTime.now());
        return kupionyBiletOkresowy;
    }

    private Passenger getPasazer() {
        Passenger p = new Passenger();
        p.setLogin("432gfds");
        p.setPassword("ewvt5ev5ry");
        p.setRole("ROLE_USER");
        return p;
    }

    private TicketSingleRide getBiletJednorazowy() {
        TicketSingleRide bilet = new TicketSingleRide();
        bilet.setPrice(1);
        bilet.setDiscountAvailable(true);
        bilet.setActive(true);
        return bilet;
    }

    private TicketPeriodic getBiletOkresowy() {
        TicketPeriodic bilet = new TicketPeriodic();
        bilet.setPrice(1);
        bilet.setDiscountAvailable(true);
        bilet.setActive(true);
        bilet.setValidityPeriod(30L);
        return bilet;
    }
}

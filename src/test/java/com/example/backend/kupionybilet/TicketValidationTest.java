package com.example.backend.kupionybilet;

import com.example.backend.bilet.model.BiletJednorazowy;
import com.example.backend.bilet.model.BiletOkresowy;
import com.example.backend.bilet.repository.BiletRepository;
import com.example.backend.kupionybilet.model.KupionyBilet;
import com.example.backend.kupionybilet.model.KupionyBiletJednorazowy;
import com.example.backend.kupionybilet.model.KupionyBiletOkresowy;
import com.example.backend.kupionybilet.model.TicketValidationRequest;
import com.example.backend.kupionybilet.repository.KupionyBiletRepository;
import com.example.backend.kupionybilet.service.KupionyBiletService;
import com.example.backend.uzytkownik.model.Pasazer;
import com.example.backend.uzytkownik.repository.PasazerRepository;
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
    private KupionyBiletRepository kupionyBiletRepositoryMock;

    @InjectMocks
    private KupionyBiletService kupionyBiletService;

    @Test
    void serviceTestNoValidatedTicket() throws Exception {
        when(kupionyBiletRepositoryMock.findByKod("1234abcd")).thenReturn(getBoughtTicket(false));
        TicketValidationRequest ticketValidationRequest = new TicketValidationRequest();
        ticketValidationRequest.setTicketId("1234abcd");
        ticketValidationRequest.setVehicleId("7654");

        boolean result = kupionyBiletService.validateTicket(ticketValidationRequest);
        assertTrue(result);
    }

    @Test
    void serviceTestValidatedTicket() throws Exception {
        when(kupionyBiletRepositoryMock.findByKod("1234abcd")).thenReturn(getBoughtTicket(true));
        TicketValidationRequest ticketValidationRequest = new TicketValidationRequest();
        ticketValidationRequest.setTicketId("1234abcd");
        ticketValidationRequest.setVehicleId("7654");

        boolean result = kupionyBiletService.validateTicket(ticketValidationRequest);
        assertFalse(result);
    }

    @Test
    void serviceTestNoTicket() throws Exception {
        when(kupionyBiletRepositoryMock.findByKod("1234abcd")).thenReturn(Optional.empty());
        TicketValidationRequest ticketValidationRequest = new TicketValidationRequest();
        ticketValidationRequest.setTicketId("1234abcd");
        ticketValidationRequest.setVehicleId("7654");

        boolean result = kupionyBiletService.validateTicket(ticketValidationRequest);
        assertFalse(result);
    }

    @Test
    void serviceTestOkresowyTicket() throws Exception {
        when(kupionyBiletRepositoryMock.findByKod("1234abcd")).thenReturn(getBoughtOkresowyTicket());
        TicketValidationRequest ticketValidationRequest = new TicketValidationRequest();
        ticketValidationRequest.setTicketId("1234abcd");
        ticketValidationRequest.setVehicleId("7654");

        boolean result = kupionyBiletService.validateTicket(ticketValidationRequest);
        assertFalse(result);
    }

    private Optional<KupionyBilet> getBoughtTicket(boolean isAlreadyValidated) {
        KupionyBiletJednorazowy kupionyBiletJednorazowy = new KupionyBiletJednorazowy();
        kupionyBiletJednorazowy.setKod("1234abcd");
        kupionyBiletJednorazowy.setDataZakupu(LocalDateTime.now());
        kupionyBiletJednorazowy.setCzyUlgowy(false);
        kupionyBiletJednorazowy.setKoncowaCena(12);
        kupionyBiletJednorazowy.setCzySkasowany(isAlreadyValidated);
        kupionyBiletJednorazowy.setPasazer(getPasazer());
        kupionyBiletJednorazowy.setBiletJednorazowy(getBiletJednorazowy());

        return Optional.of(kupionyBiletJednorazowy);
    }

    private Optional<KupionyBilet> getBoughtOkresowyTicket() {
        KupionyBiletOkresowy kupionyBiletOkresowy = new KupionyBiletOkresowy();
        kupionyBiletOkresowy.setKod("1234abcd");
        kupionyBiletOkresowy.setDataZakupu(LocalDateTime.now());
        kupionyBiletOkresowy.setCzyUlgowy(false);
        kupionyBiletOkresowy.setKoncowaCena(12);
        kupionyBiletOkresowy.setPasazer(getPasazer());
        kupionyBiletOkresowy.setBiletOkresowy(getBiletOkresowy());

        return Optional.of(kupionyBiletOkresowy);
    }

    private Pasazer getPasazer() {
        Pasazer p = new Pasazer();
        p.setLogin("432gfds");
        p.setHaslo("ewvt5ev5ry");
        p.setRole("ROLE_USER");
        return p;
    }

    private BiletJednorazowy getBiletJednorazowy() {
        BiletJednorazowy bilet = new BiletJednorazowy();
        bilet.setCena(1);
        bilet.setCzyMozliwaUlga(true);
        bilet.setActive(true);
        return bilet;
    }

    private BiletOkresowy getBiletOkresowy() {
        BiletOkresowy bilet = new BiletOkresowy();
        bilet.setCena(1);
        bilet.setCzyMozliwaUlga(true);
        bilet.setActive(true);
        bilet.setOkresWaznosci(30L);
        return bilet;
    }
}

@SpringBootTest
class TicketValidationRepoTest {
    @Autowired
    private KupionyBiletRepository kupionyBiletRepository;

    @Autowired
    private PasazerRepository pasazerRepository;

    @Autowired
    private BiletRepository biletRepository;

    @Test
    void test() throws Exception {
        Pasazer pasazer = pasazerRepository.save(getPasazer());
        KupionyBiletJednorazowy jednorazowy = getBoughtTicket();
        KupionyBiletOkresowy okresowy = getBoughtOkresowyTicket();
        jednorazowy.setPasazer(pasazer);
        okresowy.setPasazer(pasazer);

        kupionyBiletRepository.save(jednorazowy);
        kupionyBiletRepository.save(okresowy);

        Optional<KupionyBilet> kupionyBilet = kupionyBiletRepository.findByKod("1234abcd");
        Optional<KupionyBilet> kupionyBilet1 = kupionyBiletRepository.findByKod("1234");

        assertFalse(kupionyBilet.isEmpty());
        assertTrue(kupionyBilet1.isEmpty());
        assertEquals("1234abcd", kupionyBilet.get().getKod());
        assertFalse(kupionyBilet.get().isCzyUlgowy());
    }

    private KupionyBiletJednorazowy getBoughtTicket() {
        KupionyBiletJednorazowy kupionyBiletJednorazowy = new KupionyBiletJednorazowy();
        kupionyBiletJednorazowy.setKod("1234abcd");
        kupionyBiletJednorazowy.setDataZakupu(LocalDateTime.now());
        kupionyBiletJednorazowy.setCzyUlgowy(false);
        kupionyBiletJednorazowy.setKoncowaCena(12);
        kupionyBiletJednorazowy.setCzySkasowany(false);
        kupionyBiletJednorazowy.setBiletJednorazowy(biletRepository.save(getBiletJednorazowy()));

        return kupionyBiletJednorazowy;
    }

    private KupionyBiletOkresowy getBoughtOkresowyTicket() {
        KupionyBiletOkresowy kupionyBiletOkresowy = new KupionyBiletOkresowy();
        kupionyBiletOkresowy.setKod("1234abce");
        kupionyBiletOkresowy.setDataZakupu(LocalDateTime.now());
        kupionyBiletOkresowy.setCzyUlgowy(false);
        kupionyBiletOkresowy.setKoncowaCena(12);
        kupionyBiletOkresowy.setBiletOkresowy(biletRepository.save(getBiletOkresowy()));
        kupionyBiletOkresowy.setWaznyOd(LocalDateTime.now());
        kupionyBiletOkresowy.setWaznyDo(LocalDateTime.now());
        return kupionyBiletOkresowy;
    }

    private Pasazer getPasazer() {
        Pasazer p = new Pasazer();
        p.setLogin("432gfds");
        p.setHaslo("ewvt5ev5ry");
        p.setRole("ROLE_USER");
        return p;
    }

    private BiletJednorazowy getBiletJednorazowy() {
        BiletJednorazowy bilet = new BiletJednorazowy();
        bilet.setCena(1);
        bilet.setCzyMozliwaUlga(true);
        bilet.setActive(true);
        return bilet;
    }

    private BiletOkresowy getBiletOkresowy() {
        BiletOkresowy bilet = new BiletOkresowy();
        bilet.setCena(1);
        bilet.setCzyMozliwaUlga(true);
        bilet.setActive(true);
        bilet.setOkresWaznosci(30L);
        return bilet;
    }
}

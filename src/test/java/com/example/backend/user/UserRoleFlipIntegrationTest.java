package com.example.backend.user;

import com.example.backend.purchasedticket.model.PurchasedTicketSingleRide;
import com.example.backend.purchasedticket.repository.PurchasedTicketRepository;
import com.example.backend.ticket.model.TicketSingleRide;
import com.example.backend.ticket.repository.TicketRepository;
import com.example.backend.user.model.*;
import com.example.backend.user.repository.UserRepository;
import com.example.backend.user.service.UserService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class UserRoleFlipIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TicketRepository biletWzorzecRepository;

    @Autowired
    private PurchasedTicketRepository biletRepository;

    @Autowired
    private EntityManager entityManager;

    @Test
    void shouldMaintainTicketsWhenFlippingRoleBackAndForth() {
        //GIVEN
        Admin admin = new Admin();
        admin.setLogin("admin_sys"); admin.setPassword("123"); admin.setRole("ADMIN");
        userRepository.save(admin);

        TicketSingleRide wzorzec = new TicketSingleRide();
        wzorzec.setPrice(4.50); wzorzec.setAdmin(admin); wzorzec.setActive(true);
        biletWzorzecRepository.save(wzorzec);

        Passenger p = new Passenger();
        p.setLogin("tester_roli"); p.setPassword("pass123"); p.setRole("USER");
        userRepository.save(p);

        PurchasedTicketSingleRide bilet = new PurchasedTicketSingleRide();
        bilet.setPassenger(p);
        bilet.setSingleTicket(wzorzec);
        bilet.setCode("KOD_TEST_123");
        bilet.setPurchaseDate(LocalDateTime.now());
        biletRepository.save(bilet);

        entityManager.flush();
        entityManager.clear();

        //WHEN
        userService.changeUserRole(p.getId(), "INSPECTOR");

        entityManager.flush();
        entityManager.clear();

        User middleState = userRepository.findById(p.getId()).orElseThrow();
        assertEquals("INSPECTOR", middleState.getRole());
        assertInstanceOf(TicketInspector.class, middleState, "Obiekt powinien zostać rzutowany na Ticketera");

        userService.changeUserRole(p.getId(), "USER");

        entityManager.flush();
        entityManager.clear();

        //THEN
        User finalState = userRepository.findById(p.getId()).orElseThrow();

        assertInstanceOf(Passenger.class, finalState, "Powinien wrócić do bycia Pasażerem");
        Passenger recoveredPassenger = (Passenger) finalState;

        assertFalse(recoveredPassenger.getKupioneBilety().isEmpty(), "Pasażer powinien nadal mieć swój bilet!");
        boolean maPoprawnyTicket = recoveredPassenger.getKupioneBilety().stream()
                .anyMatch(b -> b.getCode().equals("KOD_TEST_123"));

        assertTrue(maPoprawnyTicket, "Pasażer powinien posiadać bilet o kodzie KOD_TEST_123");
    }
}

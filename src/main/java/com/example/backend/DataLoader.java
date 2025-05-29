package com.example.backend;

import com.example.backend.ticket.model.TicketTimeBased;
import com.example.backend.ticket.model.TicketSingleRide;
import com.example.backend.ticket.model.TicketPeriodic;
import com.example.backend.ticket.repository.TicketRepository;
import com.example.backend.user.model.TicketInspector;
import com.example.backend.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {
    @Autowired
    private final TicketRepository ticketRepository;

    @Autowired
    private final UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public DataLoader(TicketRepository ticketRepository, UserRepository userRepository) {
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        loadTicketInspectors();
        loadTIckets();
    }

    private void loadTicketInspectors() {
        if (userRepository.count() == 0) {
            TicketInspector ticketInspector = new TicketInspector();
            ticketInspector.setLogin("bileter");
            ticketInspector.setPassword(passwordEncoder.encode("1234"));
            ticketInspector.setRole("INSPECTOR");

            userRepository.save(ticketInspector);
        }
    }

    private void loadTIckets() {
        if (ticketRepository.count() == 0) {
            TicketSingleRide biletJednorazowy = new TicketSingleRide();
            biletJednorazowy.setPrice(3.20);
            biletJednorazowy.setDiscountAvailable(true);
            biletJednorazowy.setActive(true);

            TicketTimeBased biletCzasowy = new TicketTimeBased();
            biletCzasowy.setPrice(4.50);
            biletCzasowy.setDiscountAvailable(true);
            biletCzasowy.setActive(true);
            biletCzasowy.setValidityPeriod(15L);

            TicketTimeBased biletCzasowy1 = new TicketTimeBased();
            biletCzasowy1.setPrice(7.50);
            biletCzasowy1.setDiscountAvailable(true);
            biletCzasowy1.setActive(true);
            biletCzasowy1.setValidityPeriod(30L);

            TicketTimeBased biletCzasowy2 = new TicketTimeBased();
            biletCzasowy2.setPrice(12.00);
            biletCzasowy2.setDiscountAvailable(true);
            biletCzasowy2.setActive(true);
            biletCzasowy2.setValidityPeriod(60L);

            TicketPeriodic biletOkresowy = new TicketPeriodic();
            biletOkresowy.setPrice(120.00);
            biletOkresowy.setDiscountAvailable(true);
            biletOkresowy.setActive(true);
            biletOkresowy.setValidityPeriod(30L);

            TicketPeriodic biletOkresowy1 = new TicketPeriodic();
            biletOkresowy1.setPrice(220.00);
            biletOkresowy1.setDiscountAvailable(true);
            biletOkresowy1.setActive(true);
            biletOkresowy1.setValidityPeriod(60L);

            TicketPeriodic biletOkresowy2 = new TicketPeriodic();
            biletOkresowy2.setPrice(550.00);
            biletOkresowy2.setDiscountAvailable(true);
            biletOkresowy2.setActive(true);
            biletOkresowy2.setValidityPeriod(180L);

            ticketRepository.saveAll(List.of(biletJednorazowy,
                    biletCzasowy, biletCzasowy1, biletCzasowy2,
                    biletOkresowy, biletOkresowy1, biletOkresowy2));
        }
    }
}

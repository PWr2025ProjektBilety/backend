package com.example.backend;

import com.example.backend.bilet.model.Bilet;
import com.example.backend.bilet.model.BiletCzasowy;
import com.example.backend.bilet.model.BiletJednorazowy;
import com.example.backend.bilet.model.BiletOkresowy;
import com.example.backend.bilet.repository.BiletRepository;
import com.example.backend.uzytkownik.model.Bileter;
import com.example.backend.uzytkownik.repository.UzytkownikRepository;
import com.example.backend.uzytkownik.service.UzytkownikService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {
    @Autowired
    private final BiletRepository biletRepository;

    @Autowired
    private final UzytkownikRepository uzytkownikRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public DataLoader(BiletRepository biletRepository, UzytkownikRepository uzytkownikRepository) {
        this.biletRepository = biletRepository;
        this.uzytkownikRepository = uzytkownikRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        loadTicketInspectors();
        loadTIckets();
    }

    private void loadTicketInspectors() {
        if (uzytkownikRepository.count() == 0) {
            Bileter bileter = new Bileter();
            bileter.setLogin("bileter");
            bileter.setHaslo(passwordEncoder.encode("1234"));
            bileter.setRole("INSPECTOR");

            uzytkownikRepository.save(bileter);
        }
    }

    private void loadTIckets() {
        if (biletRepository.count() == 0) {
            BiletJednorazowy biletJednorazowy = new BiletJednorazowy();
            biletJednorazowy.setCena(3.20);
            biletJednorazowy.setCzyMozliwaUlga(true);
            biletJednorazowy.setActive(true);

            BiletCzasowy biletCzasowy = new BiletCzasowy();
            biletCzasowy.setCena(4.50);
            biletCzasowy.setCzyMozliwaUlga(true);
            biletCzasowy.setActive(true);
            biletCzasowy.setOkresWaznosci(15L);

            BiletCzasowy biletCzasowy1 = new BiletCzasowy();
            biletCzasowy1.setCena(7.50);
            biletCzasowy1.setCzyMozliwaUlga(true);
            biletCzasowy1.setActive(true);
            biletCzasowy1.setOkresWaznosci(30L);

            BiletCzasowy biletCzasowy2 = new BiletCzasowy();
            biletCzasowy2.setCena(12.00);
            biletCzasowy2.setCzyMozliwaUlga(true);
            biletCzasowy2.setActive(true);
            biletCzasowy2.setOkresWaznosci(60L);

            BiletOkresowy biletOkresowy = new BiletOkresowy();
            biletOkresowy.setCena(120.00);
            biletOkresowy.setCzyMozliwaUlga(true);
            biletOkresowy.setActive(true);
            biletOkresowy.setOkresWaznosci(30L);

            BiletOkresowy biletOkresowy1 = new BiletOkresowy();
            biletOkresowy1.setCena(220.00);
            biletOkresowy1.setCzyMozliwaUlga(true);
            biletOkresowy1.setActive(true);
            biletOkresowy1.setOkresWaznosci(60L);

            BiletOkresowy biletOkresowy2 = new BiletOkresowy();
            biletOkresowy2.setCena(550.00);
            biletOkresowy2.setCzyMozliwaUlga(true);
            biletOkresowy2.setActive(true);
            biletOkresowy2.setOkresWaznosci(180L);

            biletRepository.saveAll(List.of(biletJednorazowy,
                    biletCzasowy, biletCzasowy1, biletCzasowy2,
                    biletOkresowy, biletOkresowy1, biletOkresowy2));
        }
    }
}

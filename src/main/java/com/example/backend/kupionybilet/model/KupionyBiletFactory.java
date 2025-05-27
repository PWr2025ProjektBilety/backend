package com.example.backend.kupionybilet.model;

import com.example.backend.bilet.model.BiletCzasowy;
import com.example.backend.bilet.model.BiletJednorazowy;
import com.example.backend.bilet.model.BiletOkresowy;
import com.example.backend.kupionybilet.dto.NewTicketDTO;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class KupionyBiletFactory {

    private static final int DISCOUNT_PERCENTAGE = 50;

    public static KupionyBilet createKupionyBilet(NewTicketDTO dto) {

        KupionyBilet ticket = null;

        switch (dto.getTicketType()) {
            case BILET_JEDNORAZOWY:
                KupionyBiletJednorazowy jednorazowy = new KupionyBiletJednorazowy();
                jednorazowy.setCzySkasowany(false);
                BiletJednorazowy biletJednorazowy = (BiletJednorazowy) dto.getBaseTicket();
                jednorazowy.setBiletJednorazowy(biletJednorazowy);
                ticket = jednorazowy;
                break;

            case BILET_OKRESOWY:
                KupionyBiletOkresowy okresowy = new KupionyBiletOkresowy();
                okresowy.setWaznyOd(dto.getStartTime());
                BiletOkresowy biletOkresowy = (BiletOkresowy) dto.getBaseTicket();
                okresowy.setWaznyDo(dto.getStartTime().plusDays(biletOkresowy.getOkresWaznosci()));
                okresowy.setBiletOkresowy(biletOkresowy);
                ticket = okresowy;
                break;

            case BILET_CZASOWY:
                KupionyBiletCzasowy czasowy = new KupionyBiletCzasowy();
                czasowy.setCzySkasowany(false);
                BiletCzasowy biletCzasowy = (BiletCzasowy) dto.getBaseTicket();
                czasowy.setBiletCzasowy(biletCzasowy);
                ticket = czasowy;
                break;

        }
        initializeBaseFields(ticket, dto);
        return ticket;

    }

    private static void initializeBaseFields(KupionyBilet bilet, NewTicketDTO dto) {
        if(bilet != null) {
            bilet.setDataZakupu(LocalDateTime.now());
            bilet.setKod(UUID.randomUUID().toString());
            bilet.setCzyUlgowy(!dto.isNormal());
            bilet.setKoncowaCena(dto.isNormal() ? dto.getBaseTicket().getCena() :
                dto.getBaseTicket().getCena() * (1 - (DISCOUNT_PERCENTAGE / 100.0)));
        }
    }
}

package com.example.backend.uzytkownik.model;

import com.example.backend.kupionybilet.model.KupionyBiletCzasowy;
import com.example.backend.kupionybilet.model.KupionyBiletJednorazowy;
import com.example.backend.kupionybilet.model.KupionyBiletOkresowy;
import com.example.backend.ticketValidation.TicketValidationVisitor;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@DiscriminatorValue("bileter")
public class Bileter extends Uzytkownik implements TicketValidationVisitor {

    @Override
    public boolean visit(KupionyBiletJednorazowy ticket, String vehicleId) {
        return ticket.isCzySkasowany() &&
                ticket.getIdentyfikatorPojazu() != null &&
                ticket.getIdentyfikatorPojazu().equals(vehicleId);
    }

    @Override
    public boolean visit(KupionyBiletCzasowy ticket, String vehicleId) {
        return ticket.isCzySkasowany() &&
                ticket.getDataWaznosci() != null &&
                ticket.getDataWaznosci().isAfter(LocalDateTime.now());
    }

    @Override
    public boolean visit(KupionyBiletOkresowy ticket, String vehicleId) {
        return ticket.getWaznyDo() != null &&
                ticket.getWaznyDo().isAfter(LocalDateTime.now()) &&
                ticket.getWaznyOd() != null &&
                ticket.getWaznyOd().isBefore(LocalDateTime.now());
    }
}


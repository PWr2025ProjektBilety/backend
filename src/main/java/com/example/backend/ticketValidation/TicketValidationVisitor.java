package com.example.backend.ticketValidation;

import com.example.backend.kupionybilet.model.KupionyBiletCzasowy;
import com.example.backend.kupionybilet.model.KupionyBiletJednorazowy;
import com.example.backend.kupionybilet.model.KupionyBiletOkresowy;

public interface TicketValidationVisitor {

    boolean visit(KupionyBiletJednorazowy ticket, String vehicleId);
    boolean visit(KupionyBiletCzasowy ticket, String vehicleId);
    boolean visit(KupionyBiletOkresowy ticket, String vehicleId);
}

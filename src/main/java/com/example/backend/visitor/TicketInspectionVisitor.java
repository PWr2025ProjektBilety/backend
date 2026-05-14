package com.example.backend.visitor;

import com.example.backend.model.purchasedTicket.PurchasedTicketTimeBased;
import com.example.backend.model.purchasedTicket.PurchasedTicketSingleRide;
import com.example.backend.model.purchasedTicket.PurchasedTicketPeriodic;

public interface TicketInspectionVisitor {

    boolean visit(PurchasedTicketSingleRide ticket, String vehicleId);
    boolean visit(PurchasedTicketTimeBased ticket, String vehicleId);
    boolean visit(PurchasedTicketPeriodic ticket, String vehicleId);
}

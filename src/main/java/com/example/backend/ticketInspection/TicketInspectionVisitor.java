package com.example.backend.ticketInspection;

import com.example.backend.purchasedticket.model.PurchasedTicketTimeBased;
import com.example.backend.purchasedticket.model.PurchasedTicketSingleRide;
import com.example.backend.purchasedticket.model.PurchasedTicketPeriodic;

public interface TicketInspectionVisitor {

    boolean visit(PurchasedTicketSingleRide ticket, String vehicleId);
    boolean visit(PurchasedTicketTimeBased ticket, String vehicleId);
    boolean visit(PurchasedTicketPeriodic ticket, String vehicleId);
}

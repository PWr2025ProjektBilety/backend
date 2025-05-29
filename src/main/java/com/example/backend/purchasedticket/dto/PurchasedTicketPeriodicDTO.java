package com.example.backend.purchasedticket.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PurchasedTicketPeriodicDTO extends PurchasedTicketDTO {

    private LocalDateTime validFrom;
    private LocalDateTime validTo;
}

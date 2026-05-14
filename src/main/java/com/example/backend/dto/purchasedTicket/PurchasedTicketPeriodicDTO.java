package com.example.backend.dto.purchasedTicket;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PurchasedTicketPeriodicDTO extends PurchasedTicketDTO {

    private LocalDateTime validFrom;
    private LocalDateTime validTo;
}

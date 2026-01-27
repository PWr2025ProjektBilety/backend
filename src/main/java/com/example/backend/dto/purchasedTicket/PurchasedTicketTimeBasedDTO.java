package com.example.backend.dto.purchasedTicket;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PurchasedTicketTimeBasedDTO extends PurchasedTicketDTO {
    private boolean isValidated;
    private LocalDateTime validationDate;
    private LocalDateTime expirationDate;

}

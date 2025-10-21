package com.example.backend.purchasedticket.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PurchasedTicketTimeBasedDTO extends PurchasedTicketDTO {
    private boolean isValidated;
    private LocalDateTime validationDate;
    private LocalDateTime expirationDate;

}

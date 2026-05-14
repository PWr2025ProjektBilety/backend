package com.example.backend.dto.purchasedTicket;

import lombok.Data;

@Data
public class PurchasedTicketSingleBasedDTO extends PurchasedTicketDTO {

    private String vehicleId;
    private boolean isValidated;
}

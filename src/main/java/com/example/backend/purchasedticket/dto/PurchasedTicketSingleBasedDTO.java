package com.example.backend.purchasedticket.dto;

import lombok.Data;

@Data
public class PurchasedTicketSingleBasedDTO extends PurchasedTicketDTO {

    private String vehicleId;
    private boolean isValidated;
}

package com.example.backend.purchasedticket.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public  class PurchasedTicketDTO {

    private String code;
    private LocalDateTime purchaseDate;
    private boolean reduced;
    private double finalPrice;

}

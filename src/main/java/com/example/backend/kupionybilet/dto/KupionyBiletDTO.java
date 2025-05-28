package com.example.backend.kupionybilet.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public  class KupionyBiletDTO {

    private String code;
    private LocalDateTime purchaseDate;
    private boolean reduced;
    private double finalPrice;

}

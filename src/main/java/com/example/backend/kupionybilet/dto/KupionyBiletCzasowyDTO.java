package com.example.backend.kupionybilet.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class KupionyBiletCzasowyDTO extends KupionyBiletDTO {
    private boolean czySkasowany;
    private LocalDateTime dataSkasowania;
    private LocalDateTime dataWaznosci;

}

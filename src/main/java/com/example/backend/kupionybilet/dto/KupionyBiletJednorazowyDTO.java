package com.example.backend.kupionybilet.dto;

import lombok.Data;

@Data
public class KupionyBiletJednorazowyDTO extends KupionyBiletDTO {

    private String identyfikatorPojazu;
    private boolean czySkasowany;
}

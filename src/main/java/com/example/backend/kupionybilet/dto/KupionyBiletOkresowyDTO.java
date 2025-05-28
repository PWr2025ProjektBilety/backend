package com.example.backend.kupionybilet.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class KupionyBiletOkresowyDTO extends KupionyBiletDTO {

    private LocalDateTime waznyOd;
    private LocalDateTime waznyDo;
}

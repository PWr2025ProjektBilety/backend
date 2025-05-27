package com.example.backend.kupionybilet.dto;

import com.example.backend.kupionybilet.model.TicketType;
import com.example.backend.uzytkownik.dto.UserDTO;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class KupionyBiletDTO {

    private String code;
    private LocalDateTime purchaseDate;
    private boolean isNormal;
    private double finalPrice;
    private UserDTO userDTO;

}

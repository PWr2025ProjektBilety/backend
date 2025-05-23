package com.example.backend.kupionybilet.controller;

import com.example.backend.kupionybilet.model.TicketValidationRequest;
import com.example.backend.kupionybilet.service.KupionyBiletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/boughttickets")
public class KupionyBiletController {
    @Autowired
    private KupionyBiletService kupionyBiletService;

    //validate w tym kontekście to kasowanie biletu
    @PostMapping("/validate")
    public ResponseEntity<String> validateTicket(@RequestBody TicketValidationRequest ticketValidationRequest) {
        boolean isValid = kupionyBiletService.validateTicket(ticketValidationRequest);

        if (isValid) {
            return ResponseEntity.ok("Ticket successfully validated.");
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ticket validation failed.");
        }
    }
}

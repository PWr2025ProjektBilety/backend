package com.example.backend.kupionybilet.controller;

import com.example.backend.kupionybilet.dto.BuyTicketRequestDTO;
import com.example.backend.kupionybilet.model.KupionyBilet;
import com.example.backend.kupionybilet.model.TicketValidationRequest;
import com.example.backend.kupionybilet.service.KupionyBiletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("hasRole('USER')")
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

    @PostMapping("/buy")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<KupionyBilet> buyTicket(@RequestBody BuyTicketRequestDTO request, Authentication authentication) {
        String username = authentication.getName();
        KupionyBilet bilet = new KupionyBilet();
        bilet.setKod(username);
        return ResponseEntity.ok(bilet);
    }


    @GetMapping("/history")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<KupionyBilet>> getTicketHistory(Authentication authentication) {
        String username = authentication.getName();
        try {
            return ResponseEntity.ok(kupionyBiletService.getTicketHistory(username));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


}

package com.example.backend.kupionybilet.controller;

import com.example.backend.kupionybilet.dto.BuyTicketRequestDTO;
import com.example.backend.kupionybilet.dto.KupionyBiletDTO;
import com.example.backend.kupionybilet.dto.NewTicketDTO;
import com.example.backend.kupionybilet.model.KupionyBilet;
import com.example.backend.kupionybilet.model.TicketValidationRequest;
import com.example.backend.kupionybilet.service.KupionyBiletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
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
    public ResponseEntity<KupionyBiletDTO> buyTicket(@RequestBody BuyTicketRequestDTO request, Authentication authentication) {
        String username = authentication.getName();
        try{
            return ResponseEntity.ok(kupionyBiletService.buyTicket(request, username));
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    @GetMapping("/history")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<KupionyBiletDTO>> getTicketHistory(Authentication authentication,
                                                                  @RequestParam(defaultValue = "0") int page,
                                                                  @RequestParam(defaultValue = "10") int size) {
        String username = authentication.getName();
        try {
            Pageable pageable = PageRequest.of(page, size);
            return ResponseEntity.ok(kupionyBiletService.getTicketHistory(username, pageable));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


}

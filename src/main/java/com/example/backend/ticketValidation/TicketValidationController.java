package com.example.backend.ticketValidation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@PreAuthorize("hasRole('INSPECTOR')")
@RestController
@RequestMapping("/api/ticket-validation")
public class TicketValidationController {

    @Autowired
    TicketValidationService ticketValidationService;

    @PostMapping
    public ResponseEntity<Boolean> validateTicket(ValidateTicketRequestDTO request, Authentication authentication) {

        String username = authentication.getName();

        try{
            return ResponseEntity.ok(ticketValidationService.validateTicket(request, username));
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Ticket validation service is running.");
    }
}

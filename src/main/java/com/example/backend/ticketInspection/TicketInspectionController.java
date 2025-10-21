package com.example.backend.ticketInspection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@PreAuthorize("hasRole('INSPECTOR')")
@RestController
@RequestMapping("/api/ticket-inspection")
public class TicketInspectionController {

    @Autowired
    TicketInspectionService ticketInspectionService;

    @PostMapping
    public ResponseEntity<Boolean> validateTicket(@RequestBody InspectTicketRequestDTO request, Authentication authentication) {

        String username = authentication.getName();

        try{
            return ResponseEntity.ok(ticketInspectionService.validateTicket(request, username));
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Ticket inspection service is running.");
    }
}

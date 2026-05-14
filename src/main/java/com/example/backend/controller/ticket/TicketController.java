package com.example.backend.controller.ticket;

import com.example.backend.model.ticket.Ticket;
import com.example.backend.service.ticket.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@PreAuthorize("hasAnyRole('USER', 'ADMIN')")
@RestController
@RequestMapping("/api/tickets")
public class TicketController {
    @Autowired
    private TicketService ticketService;

    @GetMapping("")
    public ResponseEntity<List<Ticket>> getTickets() {
        List<Ticket> bilety = ticketService.getAllTickets();

        if (bilety.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(bilety);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<Ticket>> getAllTicketsForAdmin() {
        List<Ticket> bilety = ticketService.getAllTicketsForAdmin();
        return ResponseEntity.ok(bilety);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTicket(@PathVariable Long id) {
        ticketService.deleteTicket(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Ticket> createTicket(@RequestBody Ticket bilet, Authentication authentication) {
        String currentLogin = authentication.getName();

        Ticket savedBilet = ticketService.saveTicket(bilet, currentLogin);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedBilet);
    }
}

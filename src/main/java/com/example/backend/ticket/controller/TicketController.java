package com.example.backend.ticket.controller;

import com.example.backend.ticket.model.Ticket;
import com.example.backend.ticket.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@PreAuthorize("hasRole('USER')")
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
}

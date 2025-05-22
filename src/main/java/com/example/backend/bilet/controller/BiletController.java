package com.example.backend.bilet.controller;

import com.example.backend.bilet.model.Bilet;
import com.example.backend.bilet.service.BiletService;
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
public class BiletController {
    @Autowired
    private BiletService biletService;

    @GetMapping("")
    public ResponseEntity<List<Bilet>> getTickets() {
        List<Bilet> bilety = biletService.getAllTickets();

        if (bilety.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(bilety);
    }
}

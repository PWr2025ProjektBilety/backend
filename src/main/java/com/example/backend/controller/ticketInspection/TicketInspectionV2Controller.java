package com.example.backend.controller.ticketInspection;

import com.example.backend.dto.ticketInspection.InspectTicketBodyDTO;
import com.example.backend.dto.ticketInspection.InspectTicketResponseDTO;
import com.example.backend.service.ticketInspection.TicketInspectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@PreAuthorize("hasRole('INSPECTOR')")
@RestController
@RequestMapping("/api/tickets")
public class TicketInspectionV2Controller {

    @Autowired
    private TicketInspectionService ticketInspectionService;

    @PostMapping("/{code}/inspect")
    public ResponseEntity<InspectTicketResponseDTO> inspectTicket(
            @PathVariable String code,
            @RequestBody InspectTicketBodyDTO request,
            Authentication authentication) {

        String username = authentication.getName();

        try {
            return ResponseEntity.ok(ticketInspectionService.inspectTicket(code, request, username));
        } catch (RuntimeException e) {
            return ResponseEntity.ok(InspectTicketResponseDTO.invalid("unknown-error"));
        }
    }
}

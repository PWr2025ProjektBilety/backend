package com.example.backend.controller.purchasedTicket;

import com.example.backend.dto.purchasedTicket.BuyTicketRequestDTO;
import com.example.backend.dto.purchasedTicket.PurchasedTicketDTO;
import com.example.backend.model.purchasedTicket.TicketValidationRequest;
import com.example.backend.service.purchasedTicket.PurchasedTicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@PreAuthorize("hasRole('USER')")
@RestController
@RequestMapping("/api/boughttickets")
public class PurchasedTicketController {
    @Autowired
    private PurchasedTicketService purchasedTicketService;

    //validate w tym kontekście to kasowanie biletu
    @PostMapping("/validate")
    public ResponseEntity<String> validateTicket(@RequestBody TicketValidationRequest ticketValidationRequest) {
        boolean isValid = purchasedTicketService.validateTicket(ticketValidationRequest);

        if (isValid) {
            return ResponseEntity.ok("Ticket successfully validated.");
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Ticket validation failed.");
        }
    }

    @PostMapping("/buy")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<PurchasedTicketDTO> buyTicket(@RequestBody BuyTicketRequestDTO request, Authentication authentication) {
        String username = authentication.getName();
        try{
            return ResponseEntity.ok(purchasedTicketService.buyTicket(request, username));
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @PostMapping("/buy-with-points")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> buyTicketWithPoints(@RequestParam Long ticketId, Authentication authentication) {
        String userLogin = authentication.getName();
        try {
            PurchasedTicketDTO ticket = purchasedTicketService.buyTicketWithPoints(ticketId, userLogin);
            return ResponseEntity.ok(ticket);
        }
        catch (RuntimeException e) {
            if (e.getMessage().contains("Niewystarczająca liczba punktów")) {
                return ResponseEntity.status(HttpStatus.PAYMENT_REQUIRED)
                        .body("Niewystarczająca liczba punktów. " + e.getMessage());
            }
            else if (e.getMessage().contains("not found")) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(e.getMessage());
            }
            else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("Błąd podczas kupowania biletu: " + e.getMessage());
            }
        }
    }

    @GetMapping("/history")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Page<PurchasedTicketDTO>> getTicketHistory(Authentication authentication,
                                                                     @RequestParam(defaultValue = "0") int page,
                                                                     @RequestParam(defaultValue = "10") int size,
                                                                     @RequestParam(defaultValue = "purchaseDate,desc") String[] sort) {
        String username = authentication.getName();
        Sort sorting = Sort.by(Sort.Order.desc("purchaseDate"));
        try {
            Pageable pageable = PageRequest.of(page, size, sorting);
            return ResponseEntity.ok(purchasedTicketService.getTicketHistory(username, pageable));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


}

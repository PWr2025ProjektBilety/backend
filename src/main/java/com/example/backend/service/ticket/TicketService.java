package com.example.backend.service.ticket;

import com.example.backend.model.ticket.Ticket;
import com.example.backend.repository.ticket.TicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.backend.model.user.Admin;
import com.example.backend.repository.user.AdminRepository;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class TicketService {
    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private AdminRepository adminRepository;

    public List<Ticket> getAllTickets() {
        return ticketRepository.findByIsActiveTrue();
    }

    public List<Ticket> getAllTicketsForAdmin() {
        return ticketRepository.findAllIncludingInactive();
    }

    public void deleteTicket(Long id) {
        if (!ticketRepository.existsById(id)) {
            throw new NoSuchElementException("Ticket with given ID does not exitsts!");
        }
        ticketRepository.deleteById(id);
    }

    public Ticket saveTicket(Ticket bilet, String adminLogin) {
        Admin admin = adminRepository.findByLogin(adminLogin)
                .orElseThrow(() -> new RuntimeException("Zalogowany użytkownik nie jest adminem lub nie istnieje"));
        if (bilet.getPrice() <= 0 || bilet.getPrice() > 100000) {
            throw new IllegalArgumentException("Nieprawidłowa cena biletu");
        }

        bilet.setAdmin(admin);
        bilet.setActive(true);
        return ticketRepository.save(bilet);
    }
}

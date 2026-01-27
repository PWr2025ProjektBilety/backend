package com.example.backend.ticket;

import com.example.backend.model.ticket.Ticket;
import com.example.backend.model.ticket.TicketSingleRide;
import com.example.backend.repository.ticket.TicketRepository;
import com.example.backend.service.ticket.TicketService;
import com.example.backend.model.user.Admin;
import com.example.backend.repository.user.AdminRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TicketServiceTest {
    @Mock
    private TicketRepository biletRepository;

    @Mock
    private AdminRepository adminRepository;

    @InjectMocks
    private TicketService biletService;

    @Test
    void saveTicket_ShouldAssignAdminAndSave() {
        // GIVEN
        String login = "admin1";
        Admin admin = new Admin();
        TicketSingleRide bilet = new TicketSingleRide();
        bilet.setPrice(5.0);

        when(adminRepository.findByLogin(login)).thenReturn(Optional.of(admin));

        when(biletRepository.save((Ticket) any())).thenAnswer(invocation -> {
            return invocation.getArgument(0, Ticket.class);
        });

        // WHEN
        Ticket result = biletService.saveTicket(bilet, login);

        // THEN
        assertNotNull(result);
        assertEquals(admin, result.getAdmin());
        verify(biletRepository).save(any());
    }

    @Test
    void saveTicket_ShouldThrowException_WhenAdminNotFound() {
        // GIVEN
        String login = "nonexistent";
        when(adminRepository.findByLogin(login)).thenReturn(Optional.empty());

        // WHEN & THEN
        assertThrows(RuntimeException.class, () -> {
            biletService.saveTicket(new TicketSingleRide(), login);
        });
    }

    @Test
    void deleteTicket_ShouldCallDelete_WhenTicketExists() {
        // GIVEN
        Long id = 1L;
        when(biletRepository.existsById(id)).thenReturn(true);

        // WHEN
        biletService.deleteTicket(id);

        // THEN
        verify(biletRepository).deleteById(id);
    }

    @Test
    void deleteTicket_ShouldThrowException_WhenTicketDoesNotExist() {
        // GIVEN
        Long id = 99L;
        when(biletRepository.existsById(id)).thenReturn(false);

        // WHEN & THEN
        assertThrows(NoSuchElementException.class, () -> {
            biletService.deleteTicket(id);
        });
    }
}

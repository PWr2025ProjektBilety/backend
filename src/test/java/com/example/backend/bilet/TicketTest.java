package com.example.backend.bilet;

import com.example.backend.bilet.controller.BiletController;
import com.example.backend.bilet.model.Bilet;
import com.example.backend.bilet.model.BiletCzasowy;
import com.example.backend.bilet.model.BiletJednorazowy;
import com.example.backend.bilet.model.BiletOkresowy;
import com.example.backend.bilet.repository.BiletRepository;
import com.example.backend.bilet.service.BiletService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TicketTest {
    @InjectMocks
    private BiletService biletService;

    @InjectMocks
    private BiletController biletController;

    @Mock
    private BiletRepository biletRepositoryMock;

    @Mock
    private BiletService biletServiceMock;

    @Test
    void ticketServiceTest() throws Exception
    {
        when(biletRepositoryMock.findAll()).thenReturn(getTickets());
        List<Bilet> result = biletService.getAllTickets();

        assertEquals(3, result.size());
        double[] prices = {5, 5.5, 6};
        boolean[] active = {true, false, true};

        for(int i = 0; i < result.size(); i++) {
            assertEquals(result.get(i).getCena(), prices[i]);
            assertEquals(result.get(i).isActive(), active[i]);
        }
    }

    @Test
    void ticketControllerTest() throws Exception {
        when(biletServiceMock.getAllTickets()).thenReturn(getTickets());
        ResponseEntity<?> response = biletController.getTickets();

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(3, ((List<?>) response.getBody()).size());
    }

    @Test
    void ticketControllerEmptyTest() throws Exception {
        when(biletServiceMock.getAllTickets()).thenReturn(List.of());
        ResponseEntity<?> response = biletController.getTickets();

        assertEquals(204, response.getStatusCodeValue());
        assertNull(response.getBody());
    }

    List<Bilet> getTickets()
    {
        Bilet b1 = new BiletCzasowy();
        Bilet b2 = new BiletOkresowy();
        Bilet b3 = new BiletJednorazowy();

        b1.setCena(5);
        b1.setCzyMozliwaUlga(true);
        b1.setActive(true);

        b2.setCena(5.5);
        b2.setCzyMozliwaUlga(true);
        b2.setActive(false);

        b3.setCena(6);
        b3.setCzyMozliwaUlga(false);
        b3.setActive(true);

        return List.of(b1, b2, b3);
    }
}

package com.example.backend.user;

import com.example.backend.user.model.Admin;
import com.example.backend.user.model.*;
import com.example.backend.user.repository.UserRepository;
import com.example.backend.user.service.UserService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Mock
    private jakarta.persistence.EntityManager entityManager;

    @Mock
    private jakarta.persistence.Query query;

    @Test
    void shouldReturnAllTypesOfUsers() {
        // GIVEN
        Admin admin = new Admin();
        admin.setLogin("admin1");

        Passenger pasazer = new Passenger();
        pasazer.setLogin("pasazer1");

        List<User> mockUsers = List.of(admin, pasazer);
        when(userRepository.findAll()).thenReturn(mockUsers);

        // WHEN
        List<User> result = userService.getAllUsers();

        // THEN
        assertEquals(2, result.size());
        assertInstanceOf(Admin.class, result.get(0));
        assertInstanceOf(Passenger.class, result.get(1));
        verify(userRepository, times(1)).findAll();
    }

    @Test
    void shouldChangePassengerToBileter() {
        // GIVEN
        Long userId = 1L;
        Passenger p = new Passenger();
        p.setId(userId);
        p.setLogin("jan_kowalski");

        when(userRepository.findById(userId)).thenReturn(Optional.of(p));

        when(entityManager.createNativeQuery(anyString())).thenReturn(query);

        when(query.setParameter(anyString(), any())).thenReturn(query);
        when(query.executeUpdate()).thenReturn(1);

        // WHEN
        userService.changeUserRole(userId, "INSPECTOR");

        // THEN
        verify(entityManager, times(1)).createNativeQuery(anyString());
        verify(query, times(1)).executeUpdate();

        verify(query).setParameter("role", "INSPECTOR");
        verify(query).setParameter("type", "inspector");
        verify(query).setParameter("id", userId);
    }
}

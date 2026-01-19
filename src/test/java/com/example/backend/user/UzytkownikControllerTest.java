package com.example.backend.user;

import com.example.backend.user.controller.UserController;
import com.example.backend.user.model.Admin;
import com.example.backend.user.model.Passenger;
import com.example.backend.user.model.User;
import com.example.backend.user.service.UserService;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    void getAllUsers_ShouldReturnOkStatus() {
        // GIVEN
        List<User> users = List.of(new Admin(), new Passenger());
        when(userService.getAllUsers()).thenReturn(users);

        // WHEN
        ResponseEntity<List<User>> response = userController.getAllUsers();

        // THEN
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void getAllUsers_ShouldReturnNoContentWhenListIsEmpty() {
        // GIVEN
        when(userService.getAllUsers()).thenReturn(List.of());

        // WHEN
        ResponseEntity<List<User>> response = userController.getAllUsers();

        // THEN
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
    }
}

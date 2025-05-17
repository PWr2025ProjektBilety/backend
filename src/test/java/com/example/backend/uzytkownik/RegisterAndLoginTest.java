package com.example.backend.uzytkownik;

import com.example.backend.uzytkownik.model.LoginRequest;
import com.example.backend.uzytkownik.model.RegisterRequest;
import com.example.backend.uzytkownik.service.LogowanieUzytkownikaService;
import com.example.backend.uzytkownik.service.UzytkownikService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class RegisterAndLoginTest {
    @Autowired
    private UzytkownikService userService;

    @Test
    void registerAndLoginTest() throws Exception
    {
        registerTest();
        testLogin();
    }

    void registerTest() throws Exception {
        // given
        RegisterRequest registerRequest = new RegisterRequest("User", "Haslo1234!");
        RegisterRequest registerRequest2 = new RegisterRequest("User1", "Haslo1234!");

        // when
        boolean result = userService.registerUser(registerRequest);
        boolean result2 = userService.registerUser(registerRequest);
        boolean result3 = userService.registerUser(registerRequest2);

        // then
        assert(result);
        assert(!result2);
        assert(result3);
    }

    void testLogin()
    {
        String userName = "User";
        String pass = "Haslo1234!";
        String incorrectUserName = "User2";
        String incorrectPass = "newoufnwr";

        LoginRequest loginRequest = new LoginRequest(userName, pass);
        LoginRequest incorUserName = new LoginRequest(incorrectUserName, pass);
        LoginRequest incorPass = new LoginRequest(userName, incorrectPass);

        String loginResult = userService.authenticateAndGenerateToken(loginRequest);
        assertNotNull(loginResult);
        assertFalse(loginResult.isEmpty());

        assertThrows(BadCredentialsException.class, () -> {
            userService.authenticateAndGenerateToken(incorUserName);
        });

        assertThrows(BadCredentialsException.class, () -> {
            userService.authenticateAndGenerateToken(incorPass);
        });
    }
}

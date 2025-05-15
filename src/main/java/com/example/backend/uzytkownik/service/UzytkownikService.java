package com.example.backend.uzytkownik.service;

import com.example.backend.uzytkownik.model.LoginRequest;
import com.example.backend.uzytkownik.model.Pasazer;
import com.example.backend.uzytkownik.model.RegisterRequest;
import com.example.backend.uzytkownik.repository.PasazerRepository;
import com.example.backend.uzytkownik.repository.UzytkownikRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

@Service
public class UzytkownikService {
    @Autowired
    private UzytkownikRepository uzytkownikRepository;

    @Autowired
    private PasazerRepository pasazerRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    public boolean registerUser(RegisterRequest request) {
        if (uzytkownikRepository.existsByLogin(request.getUsername())) {
            return false;
        }

        Pasazer pasazer = new Pasazer();
        pasazer.setLogin(request.getUsername());
        pasazer.setHaslo(passwordEncoder.encode(request.getPassword()));

        pasazer.setRole("USER");
        pasazerRepository.save(pasazer);
        return true;
    }

    public String authenticateAndGenerateToken(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        User user = (User) authentication.getPrincipal();

        return jwtService.generateToken(user);
    }
}

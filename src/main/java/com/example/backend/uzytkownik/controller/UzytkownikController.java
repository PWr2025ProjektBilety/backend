package com.example.backend.uzytkownik.controller;

import com.example.backend.uzytkownik.model.LoginRequest;
import com.example.backend.uzytkownik.model.RegisterRequest;
import com.example.backend.uzytkownik.service.UzytkownikService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UzytkownikController {
    @Autowired
    private UzytkownikService uzytkownikService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        if(uzytkownikService.registerUser(request))
            return ResponseEntity.ok("User registered successfully");
        else
            return ResponseEntity.badRequest().body("User exists");
    }

    @GetMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        try {
            String jwt = uzytkownikService.authenticateAndGenerateToken(request);
            return ResponseEntity.ok().body(jwt);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid login or password");
        }
    }
}

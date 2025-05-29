package com.example.backend.user.controller;

import com.example.backend.user.model.LoginRequest;
import com.example.backend.user.model.RegisterRequest;
import com.example.backend.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {
        if(userService.registerUser(request))
            return ResponseEntity.ok("User registered successfully");
        else
            return ResponseEntity.badRequest().body("User exists");
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        try {
            String jwt = userService.authenticateAndGenerateToken(request);
            return ResponseEntity.ok().body(jwt);
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid login or password");
        }
    }
}

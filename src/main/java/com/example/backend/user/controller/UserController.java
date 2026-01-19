package com.example.backend.user.controller;

import com.example.backend.user.model.LoginRequest;
import com.example.backend.user.model.RegisterRequest;
import com.example.backend.user.model.User;
import com.example.backend.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(users);
    }

    @PatchMapping("/{id}/role")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> changeRole(@PathVariable Long id, @RequestParam String targetRole) {
        try {
            userService.changeUserRole(id, targetRole);
            return ResponseEntity.ok("Rola użytkownika została zmieniona na: " + targetRole);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Błąd podczas zmiany roli: " + e.getMessage());
        }
    }
}

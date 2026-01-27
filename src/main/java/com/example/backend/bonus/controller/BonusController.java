package com.example.backend.bonus.controller;

import com.example.backend.bonus.service.BonusService;
import com.example.backend.user.model.User;
import com.example.backend.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/bonuses")
@PreAuthorize("hasRole('USER')")
public class BonusController {
    @Autowired
    private BonusService bonusService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/balance")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Map<String, Object>> getBalance(Authentication authentication) {
        String username = authentication.getName();
        User user = userRepository.findByLogin(username)
                .orElseThrow(() -> new RuntimeException("User not found: " + username));

        Integer balance = bonusService.getPointsBalance(user);

        Map<String, Object> response = new HashMap<>();
        response.put("balance", balance);
        response.put("login", username);

        return ResponseEntity.ok(response);
    }
}

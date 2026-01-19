package com.example.backend.user.service;

import com.example.backend.user.model.LoginRequest;
import com.example.backend.user.model.Passenger;
import com.example.backend.user.model.RegisterRequest;
import com.example.backend.user.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import com.example.backend.user.repository.PassengerRepository;
import com.example.backend.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PassengerRepository passengerRepository;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private jakarta.persistence.EntityManager entityManager;

    public boolean registerUser(RegisterRequest request) {
        if (userRepository.existsByLogin(request.getUsername())) {
            return false;
        }

        Passenger passenger = new Passenger();
        passenger.setLogin(request.getUsername());
        passenger.setPassword(passwordEncoder.encode(request.getPassword()));

        passenger.setRole("USER");
        passengerRepository.save(passenger);
        return true;
    }

    public String authenticateAndGenerateToken(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        org.springframework.security.core.userdetails.User user =
                (org.springframework.security.core.userdetails.User) authentication.getPrincipal();

        return jwtService.generateToken(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Transactional
    public void changeUserRole(Long userId, String targetRole) {
        User uzytkownik = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Użytkownik nie istnieje"));

        String discriminatorValue = targetRole.equalsIgnoreCase("INSPECTOR") ? "inspector" : "passenger";

        entityManager.createNativeQuery(
                        "UPDATE \\\"user\\\" SET role = :role, type = :type WHERE id = :id")
                .setParameter("role", targetRole)
                .setParameter("type", discriminatorValue)
                .setParameter("id", userId)
                .executeUpdate();
    }
}

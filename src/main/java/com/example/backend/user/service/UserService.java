package com.example.backend.user.service;

import com.example.backend.user.model.LoginRequest;
import com.example.backend.user.model.Passenger;
import com.example.backend.user.model.RegisterRequest;
import com.example.backend.user.repository.PassengerRepository;
import com.example.backend.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

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

        User user = (User) authentication.getPrincipal();

        return jwtService.generateToken(user);
    }
}

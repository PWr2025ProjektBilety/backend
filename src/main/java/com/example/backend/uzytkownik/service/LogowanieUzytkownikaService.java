package com.example.backend.uzytkownik.service;

import com.example.backend.uzytkownik.model.Uzytkownik;
import com.example.backend.uzytkownik.repository.UzytkownikRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class LogowanieUzytkownikaService  implements UserDetailsService {
    @Autowired
    private UzytkownikRepository uzytkownikRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Uzytkownik user = uzytkownikRepository.findByLogin(username).orElseThrow(() -> new UsernameNotFoundException("Nie znaleziono użytkownika"));

        return User.builder()
                .username(user.getLogin())
                .password(user.getHaslo())
                .roles(user.getRole())
                .build();
    }
}

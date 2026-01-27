package com.example.backend.model.user;

import lombok.Getter;

@Getter
public class LoginRequest {
    private String username;
    private String password;

    public LoginRequest() {

    }

    public LoginRequest(String u, String p) {
        username = u;
        password = p;
    }
}

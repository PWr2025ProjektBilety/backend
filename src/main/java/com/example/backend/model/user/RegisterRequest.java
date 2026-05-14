package com.example.backend.model.user;

import lombok.Getter;

@Getter
public class RegisterRequest {
    private String username;
    private String password;

    public RegisterRequest() {

    }

    public RegisterRequest(String user, String pass) {
        username = user;
        password = pass;
    }
}

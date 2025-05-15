package com.example.backend.bilet.controller;

import org.springframework.security.access.prepost.PreAuthorize;

@PreAuthorize("hasRole('USER')")
public class BiletController {

}

package com.hd.GestionTareas.auth.controller;

import com.hd.GestionTareas.auth.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    @PostMapping("/login")
    public ResponseEntity<Void> authenticate(@RequestBody AuthRequest request, HttpServletResponse response) {
        service.authenticate(request, response);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterRequest request) {
        service.register(request);
        return ResponseEntity.noContent().build();
    }

}

package com.DressKlub.user_service.controller;

import com.DressKlub.user_service.dto.AuthenticationRequest;
import com.DressKlub.user_service.dto.RegisterRequest;
import com.DressKlub.user_service.model.User;
import com.DressKlub.user_service.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest request, HttpServletResponse response) {
        return authService.authenticate(request, response);
    }
}

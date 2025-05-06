package com.DressKlub.user_service.controller;

import com.DressKlub.user_service.dto.AuthenticationResponse;
import com.DressKlub.user_service.dto.LoginRequest;
import com.DressKlub.user_service.dto.RegisterRequest;
import com.DressKlub.user_service.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;


    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(userService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(@RequestBody LoginRequest request){
        return ResponseEntity.ok(userService.login(request));
    }
}

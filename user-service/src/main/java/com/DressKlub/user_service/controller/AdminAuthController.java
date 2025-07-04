package com.DressKlub.user_service.controller;


import com.DressKlub.user_service.model.User;
import com.DressKlub.user_service.repository.UserRepository;
import com.DressKlub.user_service.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
public class AdminAuthController {

    @Autowired
    private AuthService service;

    @Autowired
    private UserRepository repository;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(service.getAllUsers());
    }
}

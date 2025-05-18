package com.DressKlub.user_service.service;

import com.DressKlub.user_service.dto.AuthenticationRequest;
import com.DressKlub.user_service.dto.AuthenticationResponse;
import com.DressKlub.user_service.dto.RegisterRequest;
import com.DressKlub.user_service.exception.UserAlredyExistException;
import com.DressKlub.user_service.model.User;
import com.DressKlub.user_service.repository.UserRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AuthenticationManager authenticationManager, JwtService jwtService, UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User register(RegisterRequest request) {
        if (userRepository.existByEmail(request.getEmail())) {
            throw new UserAlredyExistException(request.getEmail() + " already exists!");
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setRoles(Set.of("ROLE_USER"));

        return userRepository.save(user);
    }

    public ResponseEntity<?> authenticate(AuthenticationRequest request, HttpServletResponse response) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        String accessToken = jwtService.generateToken(user);

        addCookie(response, "jwt", accessToken, 3600);

        return ResponseEntity.ok(new AuthenticationResponse(accessToken));
    }

    public ResponseEntity<?> logout(HttpServletResponse response) {
        clearCookie(response, "jwt");
        clearCookie(response, "refreshToken");
        return ResponseEntity.ok("Logout successfully");
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    private void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    private void clearCookie(HttpServletResponse response, String name) {
        Cookie cookie = new Cookie(name, null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}

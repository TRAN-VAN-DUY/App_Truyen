package com.moontales.controller;

import com.moontales.dto.AuthResponse;
import com.moontales.dto.LoginRequest;
import com.moontales.dto.RegisterRequest;
import com.moontales.model.User;
import com.moontales.security.JwtGenerator;
import com.moontales.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final UserService userService;
    private final JwtGenerator jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserService userService, JwtGenerator jwtService, PasswordEncoder passwordEncoder) {
        this.userService = userService; this.jwtService = jwtService; this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Validated @RequestBody RegisterRequest req) {
        if (req.getEmail() != null && userService.findByEmail(req.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email already exists");
        }
        User user;
        if (req.getDeviceId() != null && (req.getEmail() == null || req.getEmail().isBlank())) {
            user = userService.registerWithDevice(req.getDeviceId());
        } else {
            user = userService.registerWithEmail(req.getUsername(), req.getEmail(), req.getPassword());
        }
        String token = jwtService.generateToken(String.valueOf(user.getId()));
        return ResponseEntity.ok(new AuthResponse(token, user.getId(), user.getUsername()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        if (req.getDeviceId() != null && (req.getEmail() == null || req.getEmail().isBlank())) {
            Optional<User> u = userService.findByDeviceId(req.getDeviceId());
            if (u.isPresent()) {
                String token = jwtService.generateToken(String.valueOf(u.get().getId()));
                return ResponseEntity.ok(new AuthResponse(token, u.get().getId(), u.get().getUsername()));
            } else {
                User n = userService.registerWithDevice(req.getDeviceId());
                String token = jwtService.generateToken(String.valueOf(n.getId()));
                return ResponseEntity.ok(new AuthResponse(token, n.getId(), n.getUsername()));
            }
        }

        Optional<User> u = userService.findByEmail(req.getEmail());
        if (u.isEmpty()) return ResponseEntity.status(401).body("Invalid credentials");
        User user = u.get();
        if (user.getPassword() == null || !passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
        String token = jwtService.generateToken(String.valueOf(user.getId()));
        return ResponseEntity.ok(new AuthResponse(token, user.getId(), user.getUsername()));
    }
}

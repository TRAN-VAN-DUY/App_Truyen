package com.apptruyen.controller;

import com.apptruyen.dto.response.AuthResponse;
import com.apptruyen.dto.request.LoginRequest;
import com.apptruyen.dto.request.RegisterRequest;
import com.apptruyen.entity.User;
import com.apptruyen.security.JwtGenerator;
import com.apptruyen.service.UserService;
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
        String role = user.getRole() != null ? user.getRole().getRoleName() : "ROLE_USER";
        String token = jwtService.generateToken(String.valueOf(user.getId()), role);
        return ResponseEntity.ok(new AuthResponse(token, user.getId(), user.getUsername()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        if (req.getDeviceId() != null && (req.getEmail() == null || req.getEmail().isBlank())) {
            Optional<User> u = userService.findByDeviceId(req.getDeviceId());
            if (u.isPresent()) {
                String role = u.get().getRole() != null ? u.get().getRole().getRoleName() : "ROLE_USER";
                String token = jwtService.generateToken(String.valueOf(u.get().getId()), role);
                return ResponseEntity.ok(new AuthResponse(token, u.get().getId(), u.get().getUsername()));
            } else {
                User n = userService.registerWithDevice(req.getDeviceId());
                String role = n.getRole() != null ? n.getRole().getRoleName() : "ROLE_USER";
                String token = jwtService.generateToken(String.valueOf(n.getId()), role);
                return ResponseEntity.ok(new AuthResponse(token, n.getId(), n.getUsername()));
            }
        }

        Optional<User> u = userService.findByEmail(req.getEmail());
        if (u.isEmpty()) return ResponseEntity.status(401).body("Invalid credentials");
        User user = u.get();
        if (user.getPassword() == null || !passwordEncoder.matches(req.getPassword(), user.getPassword())) {
            return ResponseEntity.status(401).body("Invalid credentials");
        }
        String role = user.getRole() != null ? user.getRole().getRoleName() : "ROLE_USER";
        String token = jwtService.generateToken(String.valueOf(user.getId()), role);
        return ResponseEntity.ok(new AuthResponse(token, user.getId(), user.getUsername()));
    }
}

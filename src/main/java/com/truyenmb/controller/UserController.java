package com.truyenmb.controller;

import com.truyenmb.entity.Role;
import com.truyenmb.entity.User;
import com.truyenmb.repository.RoleRepository;
import com.truyenmb.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
public class UserController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserController(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAll() {
        return ResponseEntity.ok(userRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@PathVariable Integer id) {
        return userRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<User> create(@RequestBody User request) {
        if (request.getCreatedAt() == null) {
            request.setCreatedAt(LocalDateTime.now());
        }
        if (request.getRole() != null && request.getRole().getId() != null) {
            Role role = roleRepository.findById(request.getRole().getId())
                    .orElseThrow(() -> new IllegalArgumentException("Role not found"));
            request.setRole(role);
        }
        return ResponseEntity.ok(userRepository.save(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> update(@PathVariable Integer id, @RequestBody User request) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setDeviceId(request.getDeviceId());
                    user.setUsername(request.getUsername());
                    user.setEmail(request.getEmail());
                    user.setPassword(request.getPassword());
                    user.setAvatarUrl(request.getAvatarUrl());
                    if (request.getRole() != null && request.getRole().getId() != null) {
                        Role role = roleRepository.findById(request.getRole().getId())
                                .orElseThrow(() -> new IllegalArgumentException("Role not found"));
                        user.setRole(role);
                    }
                    return ResponseEntity.ok(userRepository.save(user));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        if (!userRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        userRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}


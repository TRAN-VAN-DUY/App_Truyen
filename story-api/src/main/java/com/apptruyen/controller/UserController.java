package com.apptruyen.controller;

import com.apptruyen.entity.Role;
import com.apptruyen.entity.User;
import com.apptruyen.repository.RoleRepository;
import com.apptruyen.repository.UserRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/admin/users")
@Tag(name = "Admin - Users", description = "CRUD quản lý người dùng (Cần ROLE_ADMIN)")
@SecurityRequirement(name = "bearerAuth")
public class UserController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserController(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @GetMapping
    public ResponseEntity<List<User>> getAll() {
        log.info("✅ UserController.getAll() được gọi - ĐÃ QUYỀN TRUY CẬP");
        List<User> users = userRepository.findAll();
        log.info("📊 Trả về {} users", users.size());
        return ResponseEntity.ok(users);
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

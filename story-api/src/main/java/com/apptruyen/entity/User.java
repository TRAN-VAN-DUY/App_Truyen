package com.apptruyen.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "users")
// @NoArgsConstructor @AllArgsConstructor
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "device_id", unique = true, length = 255)
    private String deviceId;

    @Column(name = "username", length = 100)
    private String username;

    @Column(name = "email", unique = true, length = 255)
    private String email;

    @JsonIgnore  // Không trả password về API response
    @Column(name = "password", length = 255)
    private String password;

    @Column(name = "avatar_url", length = 500)
    private String avatarUrl;

    @ManyToOne(fetch = FetchType.EAGER)  // EAGER để tránh lỗi lazy proxy khi serialize JSON
    @JoinColumn(name = "role_id", nullable = false)
    private Role role;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // // Explicit getters
    // public Integer getId() { return id; }
    // public String getUsername() { return username; }
    // public String getEmail() { return email; }
    // public String getPassword() { return password; }
    // public String getDeviceId() { return deviceId; }
    // public String getAvatarUrl() { return avatarUrl; }
    // public Role getRole() { return role; }
    // public LocalDateTime getCreatedAt() { return createdAt; }

    // // Explicit setters
    // public void setId(Integer id) { this.id = id; }
    // public void setUsername(String username) { this.username = username; }
    // public void setEmail(String email) { this.email = email; }
    // public void setPassword(String password) { this.password = password; }
    // public void setDeviceId(String deviceId) { this.deviceId = deviceId; }
    // public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }
    // public void setRole(Role role) { this.role = role; }
    // public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
}

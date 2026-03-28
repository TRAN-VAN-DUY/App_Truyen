package com.apptruyen.service;

import com.apptruyen.entity.Role;
import com.apptruyen.entity.User;
import com.apptruyen.repository.RoleRepository;
import com.apptruyen.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User registerWithEmail(String username, String email, String rawPassword) {
        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));
        Role r = roleRepository.findByRoleName("ROLE_USER").orElse(null);
        user.setRole(r);
        return userRepository.save(user);
    }

    public User registerWithDevice(String deviceId) {
        Optional<User> existing = userRepository.findByDeviceId(deviceId);
        if (existing.isPresent()) return existing.get();
        User user = new User();
        user.setDeviceId(deviceId);
        user.setUsername("GUEST");
        Role r = roleRepository.findByRoleName("ROLE_USER").orElse(null);
        user.setRole(r);
        return userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) { return userRepository.findByEmail(email); }
    public Optional<User> findByDeviceId(String deviceId) { return userRepository.findByDeviceId(deviceId); }
}

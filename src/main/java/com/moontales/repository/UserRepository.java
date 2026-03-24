package com.moontales.repository;

import com.moontales.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    Optional<User> findByDeviceId(String deviceId);
    boolean existsByEmail(String email);
}

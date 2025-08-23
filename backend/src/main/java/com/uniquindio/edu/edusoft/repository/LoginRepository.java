package com.uniquindio.edu.edusoft.repository;

import com.uniquindio.edu.edusoft.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface LoginRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
    Optional<User> findByPhone(String phone);
}
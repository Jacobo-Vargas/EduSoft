package com.uniquindio.edu.edusoft.repository;

import com.uniquindio.edu.edusoft.model.entities.CodeChangePassword;
import com.uniquindio.edu.edusoft.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CodeChangePasswordRepository extends JpaRepository<CodeChangePassword, Long> {
    Optional<CodeChangePassword> findByUser(User user);
}

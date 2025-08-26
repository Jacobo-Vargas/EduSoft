package com.uniquindio.edu.edusoft.repository;

import com.uniquindio.edu.edusoft.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, String> {

    // Buscar usuario por email
    Optional<User> findByEmail(String email);

    // Buscar usuario por documento
    Optional<User> findByDocumentNumber(String documentNumber);

    // Buscar usuario por email o documento (para validaciones)
    Optional<User> findByEmailOrDocumentNumber(String email, String documentNumber);

    // Buscar usuario por token de verificación
    Optional<User> findByVerificationToken(String verificationToken);
}

package com.uniquindio.edu.edusoft.repository;

import com.uniquindio.edu.edusoft.model.entities.CurrentStatus;
import com.uniquindio.edu.edusoft.model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CurrentStatusRepository extends JpaRepository<CurrentStatus, Long> {
    boolean existsByNameIgnoreCase(String name);
    Optional<CurrentStatus> findByName(String email);
}

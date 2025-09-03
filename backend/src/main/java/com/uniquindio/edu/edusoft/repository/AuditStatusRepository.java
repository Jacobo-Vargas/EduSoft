package com.uniquindio.edu.edusoft.repository;

import com.uniquindio.edu.edusoft.model.entities.AuditStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuditStatusRepository extends JpaRepository<AuditStatus, Long> {
    boolean existsByNameIgnoreCase(String name);

    Optional<AuditStatus> findByName(String name);
}

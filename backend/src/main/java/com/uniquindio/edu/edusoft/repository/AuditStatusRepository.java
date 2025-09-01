package com.uniquindio.edu.edusoft.repository;

import com.uniquindio.edu.edusoft.model.entities.AuditStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuditStatusRepository extends JpaRepository<AuditStatus, Long> {
    boolean existsByNameIgnoreCase(String name);
}

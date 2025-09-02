package com.uniquindio.edu.edusoft.repository;

import com.uniquindio.edu.edusoft.model.entities.CurrentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CurrentStatusRepository extends JpaRepository<CurrentStatus, Long> {
    boolean existsByNameIgnoreCase(String name);
}

package com.uniquindio.edu.edusoft.repository;

import com.uniquindio.edu.edusoft.model.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {

    @Query("SELECT c FROM Course c " +
            "JOIN FETCH c.category " +
            "JOIN FETCH c.user " +
            "JOIN FETCH c.currentStatus " +
            "JOIN FETCH c.auditStatus " +
            "WHERE c.user.id = :userId AND c.state = 'ACTIVE'")
    List<Course> findByUserIdWithRelations(@Param("userId") Long userId);

    List<Course> findByTitleContainingIgnoreCase(String title);

    Boolean existsByTitleIgnoreCase(String name);

    @Query("SELECT c FROM Course c " +
            "JOIN FETCH c.category " +
            "JOIN FETCH c.user " +
            "JOIN FETCH c.currentStatus " +
            "JOIN FETCH c.auditStatus " +
            "WHERE c.id = :id AND c.state = 'ACTIVE'")
    Optional<Course> findByIdWithRelations(@Param("id") Long id);
}

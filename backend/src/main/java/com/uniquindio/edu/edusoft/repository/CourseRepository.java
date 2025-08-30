package com.uniquindio.edu.edusoft.repository;

import com.uniquindio.edu.edusoft.model.entities.Course;
import com.uniquindio.edu.edusoft.model.enums.EnumLifecycleStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Long> {

    // Buscar cursos por título
    List<Course> findByTitleContainingIgnoreCase(String title);

    // Buscar cursos por estado
    List<Course> findByStatus(EnumLifecycleStatus status); // mejor usar enum directamente

    // Buscar cursos por autor
    List<Course> findByAuthor_Id(Long authorId);
}

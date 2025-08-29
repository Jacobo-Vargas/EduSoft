package com.uniquindio.edu.edusoft.repository;

import com.uniquindio.edu.edusoft.model.entities.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseRepository extends JpaRepository<Course, Integer> {

    // Buscar cursos por título
    List<Course> findByTitleContainingIgnoreCase(String title);

    // Buscar cursos por estado
    List<Course> findByStatus(String status);

    // Buscar cursos por autor
    List<Course> findByAuthor_IdUser(String idUser);
}

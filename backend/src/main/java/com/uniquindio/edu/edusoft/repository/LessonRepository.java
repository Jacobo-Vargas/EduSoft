package com.uniquindio.edu.edusoft.repository;

import com.uniquindio.edu.edusoft.model.entities.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long> {

    // Buscar lecciones por curso (via módulo → curso)
    List<Lesson> findByModule_Course_Id(Long courseId);

    // Buscar lecciones por módulo
    List<Lesson> findByModule_Id(Long moduleId);

    // Buscar lecciones por título
    List<Lesson> findByTitleContainingIgnoreCase(String title);
}

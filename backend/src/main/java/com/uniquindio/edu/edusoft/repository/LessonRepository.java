package com.uniquindio.edu.edusoft.repository;

import com.uniquindio.edu.edusoft.model.entities.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Integer> {

    // Buscar lecciones por curso
    List<Lesson> findByCourse_IdCourse(Integer idCourse);

    // Buscar lecciones por título
    List<Lesson> findByTitleContainingIgnoreCase(String title);
}

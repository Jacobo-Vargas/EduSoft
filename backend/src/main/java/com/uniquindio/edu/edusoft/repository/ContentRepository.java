package com.uniquindio.edu.edusoft.repository;

import com.uniquindio.edu.edusoft.model.entities.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ContentRepository extends JpaRepository<Content, Long> {

    // Contenidos sin asignar a lección de un curso
    @Query("SELECT c FROM Content c WHERE c.course.id = :courseId AND c.lesson IS NULL ORDER BY c.createdAt")
    List<Content> findUnassignedContentsByCourseId(@Param("courseId") Long courseId);

    // Contenidos de una lección ordenados
    @Query("SELECT c FROM Content c WHERE c.lesson.id = :lessonId ORDER BY c.displayOrder ASC")
    List<Content> findByLessonIdOrderByDisplayOrder(@Param("lessonId") Long lessonId);

    // Verificar si existe contenido con el mismo orden en la lección
    Optional<Content> findByLessonIdAndDisplayOrder(@Param("lessonId") Long lessonId, @Param("displayOrder") Integer displayOrder);

    // Obtener el siguiente número de orden disponible en una lección
    @Query("SELECT COALESCE(MAX(c.displayOrder), 0) + 1 FROM Content c WHERE c.lesson.id = :lessonId")
    Integer getNextDisplayOrder(@Param("lessonId") Long lessonId);

    // Contar contenidos de una lección
    long countByLessonId(@Param("lessonId") Long lessonId);
}
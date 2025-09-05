package com.uniquindio.edu.edusoft.repository;

import com.uniquindio.edu.edusoft.model.entities.Content;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ContentRepository extends JpaRepository<Content, Long> {

    List<Content> findByCourseId(Long courseId);

    List<Content> findByLessonIdOrderByDisplayOrder(Long lessonId);

    long countByLessonId(Long lessonId);

    @Query("""
   SELECT c FROM Content c
   JOIN FETCH c.lesson l
   JOIN FETCH l.module m
   JOIN FETCH m.course co
   WHERE l.id = :lessonId
   AND (c.lifecycleStatus = com.uniquindio.edu.edusoft.model.enums.EnumLifecycleStatus.BORRADOR
        OR c.lifecycleStatus = com.uniquindio.edu.edusoft.model.enums.EnumLifecycleStatus.PUBLICADO)
   ORDER BY c.displayOrder ASC
   """)
    List<Content> findActiveByLessonId(@Param("lessonId") Long lessonId);

}

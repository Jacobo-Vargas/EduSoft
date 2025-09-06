package com.uniquindio.edu.edusoft.repository;

import com.uniquindio.edu.edusoft.model.entities.Content;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ContentRepository extends JpaRepository<Content, Long> {

    List<Content> findByCourseId(Long courseId);

    List<Content> findByLessonIdOrderByDisplayOrder(Long lessonId);

    long countByLessonId(Long lessonId);
}

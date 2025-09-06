package com.uniquindio.edu.edusoft.repository;

import com.uniquindio.edu.edusoft.model.entities.CourseEvent;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseEventRepository extends JpaRepository<CourseEvent, Long> {
    @EntityGraph(attributePaths = {"user", "course", "module", "lesson", "content"})
    List<CourseEvent> findByCourseIdOrderByCreatedAtDesc(Long courseId);
}


package com.uniquindio.edu.edusoft.repository;

import com.uniquindio.edu.edusoft.model.entities.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Long> {

    // Buscar documentos por lección
    List<Document> findByLesson_Id(Long lessonId);

    // Buscar documentos por curso (navegando lesson -> module -> course)
    List<Document> findByLesson_Module_Course_Id(Long courseId);

    // Buscar documento por nombre
    List<Document> findByNameContainingIgnoreCase(String name);
}

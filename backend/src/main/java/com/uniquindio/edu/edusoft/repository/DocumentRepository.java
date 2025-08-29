package com.uniquindio.edu.edusoft.repository;

import com.uniquindio.edu.edusoft.model.entities.Document;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DocumentRepository extends JpaRepository<Document, Integer> {

    // Buscar documentos por curso
    List<Document> findByCourse_IdCourse(Integer idCourse);

    // Buscar documento por nombre
    List<Document> findByNameContainingIgnoreCase(String name);
}

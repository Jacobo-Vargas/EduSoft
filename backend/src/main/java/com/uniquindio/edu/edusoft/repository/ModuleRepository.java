package com.uniquindio.edu.edusoft.repository;

import com.uniquindio.edu.edusoft.model.entities.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Long> {

    // Buscar módulos de un curso
    List<Module> findByCourse_Id(Long courseId);

    // Buscar módulos por título
    List<Module> findByTitleIgnoreCase(String title);

    // Verificar si ya existe un módulo en un curso
    boolean existsByTitleAndCourse_Id(String title, Long courseId);
}
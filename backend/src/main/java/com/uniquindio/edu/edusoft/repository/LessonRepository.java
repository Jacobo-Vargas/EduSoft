package com.uniquindio.edu.edusoft.repository;

import com.uniquindio.edu.edusoft.model.entities.Lesson;
import com.uniquindio.edu.edusoft.model.enums.EnumLifecycleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LessonRepository extends JpaRepository<Lesson, Long> {

    @Query("""
   SELECT DISTINCT l FROM Lesson l
   JOIN FETCH l.module m
   JOIN FETCH m.course c
   LEFT JOIN FETCH l.contents ct
   WHERE m.id = :moduleId
   ORDER BY l.displayOrder ASC
   """)
    List<Lesson> findByModuleIdOrderByDisplayOrder(@Param("moduleId") Long moduleId);

    // Buscar lecciones visibles y publicadas de un módulo
    @Query("SELECT l FROM Lesson l WHERE l.module.id = :moduleId AND l.isVisible = true AND l.lifecycleStatus = :status ORDER BY l.displayOrder ASC")
    List<Lesson> findByModuleIdAndStatusAndVisible(@Param("moduleId") Long moduleId, @Param("status") EnumLifecycleStatus status);

    // Verificar si existe una lección con el mismo nombre en el módulo
    Optional<Lesson> findByModuleIdAndNameIgnoreCase(@Param("moduleId") Long moduleId, @Param("name") String name);

    // Verificar si existe una lección con el mismo orden en el módulo
    Optional<Lesson> findByModuleIdAndDisplayOrder(@Param("moduleId") Long moduleId, @Param("displayOrder") Integer displayOrder);

    // Contar lecciones de un módulo
    long countByModuleId(@Param("moduleId") Long moduleId);

    // Obtener el siguiente número de orden disponible
    @Query("SELECT COALESCE(MAX(l.displayOrder), 0) + 1 FROM Lesson l WHERE l.module.id = :moduleId")
    Integer getNextDisplayOrder(@Param("moduleId") Long moduleId);

    // Buscar lecciones por curso
    @Query("SELECT l FROM Lesson l JOIN FETCH l.module WHERE l.module.id = :moduleId")
    List<Lesson> findByModuleId(@Param("moduleId") Long moduleId);
}
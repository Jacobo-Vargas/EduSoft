package com.uniquindio.edu.edusoft.repository;

import com.uniquindio.edu.edusoft.model.entities.Module;
import com.uniquindio.edu.edusoft.model.enums.EnumLifecycleStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ModuleRepository extends JpaRepository<Module, Long> {


    @Query("SELECT m FROM Module m JOIN FETCH m.course WHERE m.id = :id")
    Optional<Module> findByIdWithCourse(@Param("id") Long id);

    @Query("SELECT m FROM Module m JOIN FETCH m.course WHERE m.course.id = :courseId")
    List<Module> findByCourseIdWithCourse(@Param("courseId") Long courseId);

    // Buscar módulos de un curso específico ordenados por displayOrder
    @Query("SELECT m FROM Module m WHERE m.course.id = :courseId ORDER BY m.displayOrder ASC")
    List<Module> findByCourseIdOrderByDisplayOrder(@Param("courseId") Long courseId);

    // Buscar módulos visibles y publicados de un curso
    @Query("SELECT m FROM Module m WHERE m.course.id = :courseId AND m.isVisible = true AND m.lifecycleStatus = :status ORDER BY m.displayOrder ASC")
    List<Module> findByCourseIdAndStatusAndVisible(@Param("courseId") Long courseId, @Param("status") EnumLifecycleStatus status);

    // Verificar si existe un módulo con el mismo nombre en el curso
    Optional<Module> findByCourseIdAndNameIgnoreCase(@Param("courseId") Long courseId, @Param("name") String name);

    // Verificar si existe un módulo con el mismo orden en el curso
    Optional<Module> findByCourseIdAndDisplayOrder(@Param("courseId") Long courseId, @Param("displayOrder") Integer displayOrder);

    // Contar módulos de un curso
    long countByCourseId(@Param("courseId") Long courseId);

    // Obtener el siguiente número de orden disponible
    @Query("SELECT COALESCE(MAX(m.displayOrder), 0) + 1 FROM Module m WHERE m.course.id = :courseId")
    Integer getNextDisplayOrder(@Param("courseId") Long courseId);

    @Query("SELECT m FROM Module m JOIN FETCH m.course " +
            "WHERE m.course.id = :courseId " +
            "AND (m.lifecycleStatus = com.uniquindio.edu.edusoft.model.enums.EnumLifecycleStatus.PUBLICADO " +
            "     OR m.lifecycleStatus = com.uniquindio.edu.edusoft.model.enums.EnumLifecycleStatus.BORRADOR) " +
            "ORDER BY m.displayOrder ASC")
    List<Module> findActiveModulesByCourseId(@Param("courseId") Long courseId);
}
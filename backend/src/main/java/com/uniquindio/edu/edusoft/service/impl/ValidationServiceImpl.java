package com.uniquindio.edu.edusoft.service.impl;

import com.uniquindio.edu.edusoft.model.entities.*;
import com.uniquindio.edu.edusoft.model.entities.Module;
import com.uniquindio.edu.edusoft.model.enums.EnumLifecycleStatus;
import com.uniquindio.edu.edusoft.model.enums.EnumUserType;
import com.uniquindio.edu.edusoft.repository.*;
import com.uniquindio.edu.edusoft.service.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ValidationServiceImpl implements ValidationService {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final ModuleRepository moduleRepository;
    private final LessonRepository lessonRepository;
    private final ContentRepository contentRepository;

    @Override
    public User validateTeacher(String userId) throws Exception {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        if (user.getUserType() != EnumUserType.PROFESOR) {
            throw new IllegalArgumentException("Solo los profesores pueden realizar esta acción");
        }

        return user;
    }

    @Override
    public Course validateCourseOwnership(Long courseId, String userId) throws Exception {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado"));

        if (!course.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("No tiene permisos para acceder a este curso");
        }

        return course;
    }

    @Override
    public Module validateModuleOwnership(Long moduleId, String userId) throws Exception {
        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new IllegalArgumentException("Módulo no encontrado"));

        if (!module.getCourse().getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("No tiene permisos para acceder a este módulo");
        }

        return module;
    }

    @Override
    public Lesson validateLessonOwnership(Long lessonId, String userId) throws Exception {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new IllegalArgumentException("Lección no encontrada"));

        if (!lesson.getModule().getCourse().getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("No tiene permisos para acceder a esta lección");
        }

        return lesson;
    }


    @Override
    public void validatePublicationRules(Module module) throws Exception {
        // No se puede publicar si el curso padre está eliminado
        if (module.getCourse().getCurrentStatus() != null &&
                module.getCourse().getCurrentStatus().getName().equals("ELIMINADO")) {
            throw new IllegalArgumentException("No se puede publicar un módulo de un curso eliminado");
        }

        // Para publicar un módulo debe tener al menos una lección publicable
        if (module.getLifecycleStatus() == EnumLifecycleStatus.PUBLICADO) {
            long publishableLessons = lessonRepository.findByModuleIdOrderByDisplayOrder(module.getId())
                    .stream()
                    .filter(lesson -> lesson.getLifecycleStatus() != EnumLifecycleStatus.ELIMINADO)
                    .filter(lesson -> lesson.getIsVisible())
                    .count();

            if (publishableLessons == 0) {
                throw new IllegalArgumentException("Un módulo debe tener al menos una lección visible para ser publicado");
            }
        }
    }

    @Override
    public void validatePublicationRules(Lesson lesson) throws Exception {
        // No se puede publicar si el módulo padre está eliminado
        if (lesson.getModule().getLifecycleStatus() == EnumLifecycleStatus.ELIMINADO) {
            throw new IllegalArgumentException("No se puede publicar una lección de un módulo eliminado");
        }

        // Para publicar una lección debe tener al menos un contenido visible
        if (lesson.getLifecycleStatus() == EnumLifecycleStatus.PUBLICADO) {
            long visibleContents = contentRepository.findByLessonIdOrderByDisplayOrder(lesson.getId())
                    .stream()
                    .filter(content -> content.getIsVisible())
                    .count();

            if (visibleContents == 0) {
                throw new IllegalArgumentException("Una lección debe tener al menos un contenido visible para ser publicada");
            }
        }
    }
}
package com.uniquindio.edu.edusoft.service.impl;

import com.uniquindio.edu.edusoft.model.dto.lesson.LessonRequestDto;
import com.uniquindio.edu.edusoft.model.dto.lesson.LessonResponseDto;
import com.uniquindio.edu.edusoft.model.dto.common.ReorderRequestDto;
import com.uniquindio.edu.edusoft.model.dto.content.ContentAssignmentDto;
import com.uniquindio.edu.edusoft.model.entities.*;
import com.uniquindio.edu.edusoft.model.entities.Module;
import com.uniquindio.edu.edusoft.model.enums.EnumLifecycleStatus;
import com.uniquindio.edu.edusoft.model.enums.EnumState;
import com.uniquindio.edu.edusoft.model.enums.EnumUserType;
import com.uniquindio.edu.edusoft.model.mapper.LessonMapper;
import com.uniquindio.edu.edusoft.repository.*;
import com.uniquindio.edu.edusoft.service.LessonService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LessonServiceImpl implements LessonService {

    private final LessonRepository lessonRepository;
    private final ModuleRepository moduleRepository;
    private final ContentRepository contentRepository;
    private final UserRepository userRepository;
    private final LessonMapper lessonMapper;

    @Override
    @Transactional
    public ResponseEntity<LessonResponseDto> createLesson(LessonRequestDto lessonRequestDto, String userId) throws Exception {
        User user = validateTeacher(userId);
        Module module = validateModuleOwnership(lessonRequestDto.getModuleId(), userId);

        // Validar nombre único en el módulo
        Optional<Lesson> existingLesson = lessonRepository.findByModuleIdAndNameIgnoreCase(
                lessonRequestDto.getModuleId(), lessonRequestDto.getName());
        if (existingLesson.isPresent()) {
            return ResponseEntity.badRequest().body(null);
        }

        // Asignar orden si no se especifica
        if (lessonRequestDto.getDisplayOrder() == null) {
            lessonRequestDto.setDisplayOrder(lessonRepository.getNextDisplayOrder(lessonRequestDto.getModuleId()));
        } else {
            // Verificar que el orden no esté ocupado
            Optional<Lesson> existingOrder = lessonRepository.findByModuleIdAndDisplayOrder(
                    lessonRequestDto.getModuleId(), lessonRequestDto.getDisplayOrder());
            if (existingOrder.isPresent()) {
                return ResponseEntity.badRequest().body(null);
            }
        }

        Lesson lesson = lessonMapper.toEntity(lessonRequestDto);
        lesson.setModule(module);

        Lesson savedLesson = lessonRepository.save(lesson);
        return ResponseEntity.ok(lessonMapper.toResponseDto(savedLesson));
    }

    @Override
    @Transactional
    public ResponseEntity<LessonResponseDto> updateLesson(Long lessonId, LessonRequestDto lessonRequestDto, String userId) throws Exception {
        User user = validateTeacher(userId);
        Lesson lesson = validateLessonOwnership(lessonId, userId);

        // Validar nombre único en el módulo (excepto la actual)
        Optional<Lesson> existingLesson = lessonRepository.findByModuleIdAndNameIgnoreCase(
                lesson.getModule().getId(), lessonRequestDto.getName());
        if (existingLesson.isPresent() && !existingLesson.get().getId().equals(lessonId)) {
            return ResponseEntity.badRequest().body(null);
        }

        // Actualizar campos
        lesson.setName(lessonRequestDto.getName());
        lesson.setDescription(lessonRequestDto.getDescription());

        if (lessonRequestDto.getLifecycleStatus() != null) {
            lesson.setLifecycleStatus(lessonRequestDto.getLifecycleStatus());
        }

        if (lessonRequestDto.getIsVisible() != null) {
            lesson.setIsVisible(lessonRequestDto.getIsVisible());
        }

        Lesson savedLesson = lessonRepository.save(lesson);
        return ResponseEntity.ok(lessonMapper.toResponseDto(savedLesson));
    }

    @Override
    @Transactional
    public ResponseEntity<?> deleteLesson(Long lessonId, String userId) throws Exception {
        User user = validateTeacher(userId);
        Lesson lesson = validateLessonOwnership(lessonId, userId);

        lesson.setLifecycleStatus(EnumLifecycleStatus.ELIMINADO);
        lesson.setDeletedAt(new Date());
        lesson.setUpdatedAt(new Date());
        lessonRepository.save(lesson);
        return ResponseEntity.ok("Lección eliminada correctamente");
    }



    @Override
    public ResponseEntity<List<LessonResponseDto>> getLessonsByModule(Long moduleId) throws Exception {
        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new IllegalArgumentException("Módulo no encontrado"));

        List<Lesson> lessons = lessonRepository.findByModuleIdOrderByDisplayOrder(moduleId);

        if (lessons.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204
        }

        List<LessonResponseDto> responseDtos = lessonMapper.toResponseDtoList(lessons);
        return ResponseEntity.ok(responseDtos);
    }

    @Override
    public ResponseEntity<LessonResponseDto> getLessonById(Long lessonId) throws Exception {
        Optional<Lesson> lessonOpt = lessonRepository.findById(lessonId);
        if (lessonOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        LessonResponseDto responseDto = lessonMapper.toResponseDto(lessonOpt.get());
        return ResponseEntity.ok(responseDto);
    }

    @Override
    @Transactional
    public ResponseEntity<?> reorderLessons(Long moduleId, ReorderRequestDto reorderRequest, String userId) throws Exception {
        User user = validateTeacher(userId);
        Module module = validateModuleOwnership(moduleId, userId);

        // Validar que todos los IDs existan y pertenezcan al módulo
        List<Long> lessonIds = reorderRequest.getItems().stream()
                .map(ReorderRequestDto.ReorderItemDto::getId)
                .collect(Collectors.toList());

        List<Lesson> lessons = lessonRepository.findAllById(lessonIds);
        if (lessons.size() != lessonIds.size()) {
            return ResponseEntity.badRequest().body("Algunas lecciones no existen");
        }

        // Verificar que todas las lecciones pertenecen al módulo
        boolean allBelongToModule = lessons.stream()
                .allMatch(lesson -> lesson.getModule().getId().equals(moduleId));

        if (!allBelongToModule) {
            return ResponseEntity.badRequest().body("Algunas lecciones no pertenecen al módulo");
        }

        // Validar órdenes únicos
        List<Integer> newOrders = reorderRequest.getItems().stream()
                .map(ReorderRequestDto.ReorderItemDto::getNewOrder)
                .collect(Collectors.toList());

        if (newOrders.size() != newOrders.stream().distinct().count()) {
            return ResponseEntity.badRequest().body("Órdenes duplicados no permitidos");
        }

        // Aplicar reordenamiento
        for (ReorderRequestDto.ReorderItemDto item : reorderRequest.getItems()) {
            Lesson lesson = lessons.stream()
                    .filter(l -> l.getId().equals(item.getId()))
                    .findFirst()
                    .orElseThrow();

            lesson.setDisplayOrder(item.getNewOrder());
        }

        lessonRepository.saveAll(lessons);
        return ResponseEntity.ok().build();
    }

    @Override
    @Transactional
    public ResponseEntity<?> moveLessonToModule(Long lessonId, Long newModuleId, String userId) throws Exception {
        User user = validateTeacher(userId);
        Lesson lesson = validateLessonOwnership(lessonId, userId);
        Module newModule = validateModuleOwnership(newModuleId, userId);

        // Verificar que ambos módulos pertenezcan al mismo curso
        if (!lesson.getModule().getCourse().getId().equals(newModule.getCourse().getId())) {
            return ResponseEntity.badRequest().body("Los módulos deben pertenecer al mismo curso");
        }

        // Verificar nombre único en el módulo destino
        Optional<Lesson> existingLesson = lessonRepository.findByModuleIdAndNameIgnoreCase(
                newModuleId, lesson.getName());
        if (existingLesson.isPresent()) {
            return ResponseEntity.badRequest().body("Ya existe una lección con ese nombre en el módulo destino");
        }

        // Asignar nuevo módulo y orden
        lesson.setModule(newModule);
        lesson.setDisplayOrder(lessonRepository.getNextDisplayOrder(newModuleId));

        lessonRepository.save(lesson);
        return ResponseEntity.ok().build();
    }

    @Override
    @Transactional
    public ResponseEntity<?> toggleLessonVisibility(Long lessonId, String userId) throws Exception {
        User user = validateTeacher(userId);
        Lesson lesson = validateLessonOwnership(lessonId, userId);

        lesson.setIsVisible(!lesson.getIsVisible());
        lessonRepository.save(lesson);

        return ResponseEntity.ok().build();
    }

    @Override
    @Transactional
    public ResponseEntity<?> changeLessonStatus(Long lessonId, String status, String userId) throws Exception {
        User user = validateTeacher(userId);
        Lesson lesson = validateLessonOwnership(lessonId, userId);

        try {
            EnumLifecycleStatus lifecycleStatus = EnumLifecycleStatus.valueOf(status.toUpperCase());
            lesson.setLifecycleStatus(lifecycleStatus);
            lessonRepository.save(lesson);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Estado inválido");
        }
    }

    @Override
    @Transactional
    public ResponseEntity<?> assignContentsToLesson(ContentAssignmentDto assignmentDto, String userId) throws Exception {
        User user = validateTeacher(userId);
        Lesson lesson = validateLessonOwnership(assignmentDto.getLessonId(), userId);

        // Obtener contenidos
        List<Long> contentIds = assignmentDto.getContents().stream()
                .map(ContentAssignmentDto.ContentOrderDto::getContentId)
                .collect(Collectors.toList());

        List<Content> contents = contentRepository.findAllById(contentIds);
        if (contents.size() != contentIds.size()) {
            return ResponseEntity.badRequest().body("Algunos contenidos no existen");
        }

        // Verificar que todos los contenidos pertenecen al mismo curso
        Long courseId = lesson.getModule().getCourse().getId();
        boolean allBelongToCourse = contents.stream()
                .allMatch(content -> content.getCourse().getId().equals(courseId));

        if (!allBelongToCourse) {
            return ResponseEntity.badRequest().body("Los contenidos deben pertenecer al mismo curso");
        }

        // Validar órdenes únicos
        List<Integer> orders = assignmentDto.getContents().stream()
                .map(ContentAssignmentDto.ContentOrderDto::getDisplayOrder)
                .collect(Collectors.toList());

        if (orders.size() != orders.stream().distinct().count()) {
            return ResponseEntity.badRequest().body("Órdenes duplicados no permitidos");
        }

        // Asignar contenidos a la lección
        for (ContentAssignmentDto.ContentOrderDto contentOrder : assignmentDto.getContents()) {
            Content content = contents.stream()
                    .filter(c -> c.getId().equals(contentOrder.getContentId()))
                    .findFirst()
                    .orElseThrow();

            content.setLesson(lesson);
            content.setDisplayOrder(contentOrder.getDisplayOrder());
        }

        contentRepository.saveAll(contents);
        return ResponseEntity.ok().build();
    }

    @Override
    @Transactional
    public ResponseEntity<?> unassignContentFromLesson(Long contentId, String userId) throws Exception {
        User user = validateTeacher(userId);

        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new IllegalArgumentException("Contenido no encontrado"));

        if (content.getLesson() == null) {
            return ResponseEntity.badRequest().body("El contenido no está asignado a ninguna lección");
        }

        // Verificar permisos
        if (!content.getCourse().getUser().getId().equals(userId)) {
            return ResponseEntity.badRequest().body("No tiene permisos para modificar este contenido");
        }

        content.setLesson(null);
        content.setDisplayOrder(null);
        contentRepository.save(content);

        return ResponseEntity.ok().build();
    }

    @Override
    @Transactional
    public ResponseEntity<?> reorderContentsInLesson(Long lessonId, ReorderRequestDto reorderRequest, String userId) throws Exception {
        User user = validateTeacher(userId);
        Lesson lesson = validateLessonOwnership(lessonId, userId);

        // Validar que todos los IDs existan y pertenezcan a la lección
        List<Long> contentIds = reorderRequest.getItems().stream()
                .map(ReorderRequestDto.ReorderItemDto::getId)
                .collect(Collectors.toList());

        List<Content> contents = contentRepository.findAllById(contentIds);
        if (contents.size() != contentIds.size()) {
            return ResponseEntity.badRequest().body("Algunos contenidos no existen");
        }

        // Verificar que todos los contenidos pertenecen a la lección
        boolean allBelongToLesson = contents.stream()
                .allMatch(content -> content.getLesson() != null && content.getLesson().getId().equals(lessonId));

        if (!allBelongToLesson) {
            return ResponseEntity.badRequest().body("Algunos contenidos no pertenecen a la lección");
        }

        // Validar órdenes únicos
        List<Integer> newOrders = reorderRequest.getItems().stream()
                .map(ReorderRequestDto.ReorderItemDto::getNewOrder)
                .collect(Collectors.toList());

        if (newOrders.size() != newOrders.stream().distinct().count()) {
            return ResponseEntity.badRequest().body("Órdenes duplicados no permitidos");
        }

        // Aplicar reordenamiento
        for (ReorderRequestDto.ReorderItemDto item : reorderRequest.getItems()) {
            Content content = contents.stream()
                    .filter(c -> c.getId().equals(item.getId()))
                    .findFirst()
                    .orElseThrow();

            content.setDisplayOrder(item.getNewOrder());
        }

        contentRepository.saveAll(contents);
        return ResponseEntity.ok().build();
    }

    private User validateTeacher(String userEmail) throws Exception {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        if (user.getUserType() != EnumUserType.PROFESOR) {
            throw new IllegalArgumentException("Solo los profesores pueden gestionar lecciones");
        }

        return user;
    }

    private Module validateModuleOwnership(Long moduleId, String userEmail) throws Exception {
        Module module = moduleRepository.findById(moduleId)
                .orElseThrow(() -> new IllegalArgumentException("Módulo no encontrado"));

        if (!module.getCourse().getUser().getEmail().equals(userEmail)) {
            throw new IllegalArgumentException("No tiene permisos para modificar este módulo");
        }

        return module;
    }

    private Lesson validateLessonOwnership(Long lessonId, String userId) throws Exception {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new IllegalArgumentException("Lección no encontrada"));

        if (!lesson.getModule().getCourse().getUser().getEmail().equals(userId)) {
            throw new IllegalArgumentException("No tiene permisos para modificar esta lección");
        }

        return lesson;
    }
}
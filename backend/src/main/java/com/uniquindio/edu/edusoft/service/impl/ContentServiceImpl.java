package com.uniquindio.edu.edusoft.service.impl;

import com.uniquindio.edu.edusoft.model.dto.content.ContentRequestDto;
import com.uniquindio.edu.edusoft.model.dto.content.ContentResponseDto;
import com.uniquindio.edu.edusoft.model.dto.content.ContentAssignmentDto;
import com.uniquindio.edu.edusoft.model.entities.Content;
import com.uniquindio.edu.edusoft.model.entities.Lesson;
import com.uniquindio.edu.edusoft.model.entities.User;
import com.uniquindio.edu.edusoft.model.enums.EnumCourseEventType;
import com.uniquindio.edu.edusoft.model.enums.EnumLifecycleStatus;
import com.uniquindio.edu.edusoft.model.enums.EnumUserType;
import com.uniquindio.edu.edusoft.repository.ContentRepository;
import com.uniquindio.edu.edusoft.repository.LessonRepository;
import com.uniquindio.edu.edusoft.repository.UserRepository;
import com.uniquindio.edu.edusoft.service.ContentService;
import com.uniquindio.edu.edusoft.model.mapper.ContentMapper;
import com.uniquindio.edu.edusoft.utils.BaseResponse;
import com.uniquindio.edu.edusoft.utils.CourseEventUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContentServiceImpl implements ContentService {

    private final ContentRepository contentRepository;
    private final LessonRepository lessonRepository;
    private final UserRepository userRepository;
    private final ContentMapper contentMapper;
    private final CourseEventUtil courseEventUtil;


    @Override
    @Transactional
    public ResponseEntity<ContentResponseDto> createContent(ContentRequestDto request, String userEmail) throws Exception {
        Lesson lesson = lessonRepository.findById(request.getLessonId())
                .orElseThrow(() -> new IllegalArgumentException("Lección no encontrada"));

        // Validar que el profesor dueño del curso es el que crea el contenido
        if (!lesson.getModule().getCourse().getUser().getEmail().equals(userEmail)) {
            throw new IllegalArgumentException("No tiene permisos para agregar contenido en esta lección");
        }

        Content content = contentMapper.toEntity(request);
        content.setLesson(lesson);
        content.setCourse(lesson.getModule().getCourse());

        Content saved = contentRepository.save(content);
        courseEventUtil.registerEvent(
                lesson.getModule().getCourse(),
                lesson.getModule(),
                lesson,
                saved,
                lesson.getModule().getCourse().getUser(),
                EnumCourseEventType.CONTENT_CREATED,
                "Se creó el contenido: " + saved.getTitle()
        );
        return ResponseEntity.ok(contentMapper.toResponseDto(saved));
    }

    @Override
    public ResponseEntity<List<ContentResponseDto>> getContentsByCourse(Long courseId) {
        List<ContentResponseDto> contents = contentRepository.findByCourseId(courseId)
                .stream()
                .map(contentMapper::toResponseDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(contents);
    }

    @Override
    public ResponseEntity<List<ContentResponseDto>> getContentsByLesson(Long lessonId) {
        List<ContentResponseDto> contents = contentRepository.findActiveByLessonId(lessonId)
                .stream()
                .map(contentMapper::toResponseDto)
                .collect(Collectors.toList());

        if (contents.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(contents);
    }

    @Override
    @Transactional
    public ResponseEntity<?> assignContentsToLesson(ContentAssignmentDto dto, String userEmail) {
        Lesson lesson = lessonRepository.findById(dto.getLessonId())
                .orElseThrow(() -> new IllegalArgumentException("Lección no encontrada"));

        validateTeacher(userEmail);

        List<Content> contents = contentRepository.findAllById(
                dto.getContents().stream().map(ContentAssignmentDto.ContentOrderDto::getContentId).toList()
        );

        contents.forEach(c -> {
            c.setLesson(lesson);
            c.setDisplayOrder(dto.getContents().stream()
                    .filter(d -> d.getContentId().equals(c.getId()))
                    .findFirst()
                    .get()
                    .getDisplayOrder());
        });

        contentRepository.saveAll(contents);
        return ResponseEntity.ok("Contenidos asignados correctamente");
    }

    @Override
    @Transactional
    public ResponseEntity<?> unassignContent(Long contentId, String userEmail) {
        validateTeacher(userEmail);

        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new IllegalArgumentException("Contenido no encontrado"));

        content.setLesson(null);
        content.setDisplayOrder(null);

        contentRepository.save(content);
        return ResponseEntity.ok("Contenido desasignado");
    }

    @Override
    @Transactional
    public ResponseEntity<?> deleteContent(Long contentId, String userEmail) {
        validateTeacher(userEmail);

        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new IllegalArgumentException("Contenido no encontrado"));

        // Soft delete
        content.setLifecycleStatus(EnumLifecycleStatus.ELIMINADO);
        content.setDeletedAt(new Date());
        content.setUpdatedAt(new Date());
        contentRepository.save(content);

        courseEventUtil.registerEvent(
                content.getCourse(),
                content.getLesson() != null ? content.getLesson().getModule() : null,
                content.getLesson(),
                content,
                content.getCourse().getUser(),
                EnumCourseEventType.CONTENT_DELETED,
                "Se eliminó el contenido: " + content.getTitle()
        );

        return BaseResponse.response("Contenido eliminado correctamente", "SUCCESS");
    }


    private User validateTeacher(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        if (user.getUserType() != EnumUserType.PROFESOR) {
            throw new IllegalArgumentException("Solo profesores pueden gestionar contenidos");
        }

        return user;
    }
}

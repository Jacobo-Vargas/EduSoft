package com.uniquindio.edu.edusoft.service.impl;

import com.uniquindio.edu.edusoft.model.dto.course.CourseRequestDto;
import com.uniquindio.edu.edusoft.model.entities.*;
import com.uniquindio.edu.edusoft.model.enums.EnumState;
import com.uniquindio.edu.edusoft.model.mapper.CourseMapper;
import com.uniquindio.edu.edusoft.repository.*;
import com.uniquindio.edu.edusoft.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final AuditStatusRepository auditStatusRepository;
    private final CurrentStatusRepository currentStatusRepository;
    private final CloudinaryService cloudinaryService;

    @Override
    public ResponseEntity<?> createCourse(CourseRequestDto courseRequestDto) throws Exception {
        // Validar campos
        String messageValidate = validateFilds(courseRequestDto);
        if (!messageValidate.isEmpty()) {
            return ResponseEntity.badRequest().body(messageValidate);
        }
        // Mapear DTO → Entity
        Course course = courseMapper.toEntity(courseRequestDto);
        if (courseRepository.existsByTitleIgnoreCase(course.getTitle())) {
            throw new IllegalArgumentException("El título ingresado ya ha sido registrado en otro curso");
        }
        // Validar FK category
        Category category = categoryRepository.findById(courseRequestDto.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada"));
        course.setCategory(category);
        // Validar FK profesor
        User profesor = userRepository.findById(courseRequestDto.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Profesor no encontrado"));
        course.setUser(profesor);
       Optional<CurrentStatus> existCurrentStatus = currentStatusRepository.findByName("borrador");
        if(existCurrentStatus.isPresent()){
            course.setCurrentStatus(existCurrentStatus.get());
        }else {
            CurrentStatus currentStatus = new CurrentStatus();
            currentStatus.setName("borrador");
            currentStatus.setDescription("Estado  en el cual los cursos se encontran si no han sido publicados");
            currentStatusRepository.save(currentStatus);
            course.setCurrentStatus(currentStatus);
        }
        Optional<AuditStatus> existAudiStatus = auditStatusRepository.findByName("sin-revision");
        if(existAudiStatus.isPresent()){
            course.setAuditStatus(existAudiStatus.get());
        }else {
            AuditStatus auditStatus = new AuditStatus();
            auditStatus.setName("sin-revision");
            auditStatus.setDescription("Estado  en el cual los cursos se encontran si no han sido revisados por auditoria");
            auditStatusRepository.save(auditStatus);
            course.setAuditStatus(auditStatus);
        }
        if (courseRequestDto.getCoverUrl() != null && !courseRequestDto.getCoverUrl().isEmpty()) {
            Map uploadResult = cloudinaryService.uploadFile(courseRequestDto.getCoverUrl());
            String imageUrl = (String) uploadResult.get("secure_url");
            course.setCoverUrl(imageUrl); // asume que Course tiene campo coverUrl
        }
        if(course.getPrice()==null){
            course.setPrice(BigDecimal.ZERO);
        }
        // Guardar curso
        Course saved = courseRepository.save(course);
        // Retornar respuesta con DTO enriquecido
        return ResponseEntity.ok(courseMapper.toResponseDto(saved));
    }

    @Override
    public ResponseEntity<?> getCoursesByUser(Long userId) {
        List<Course> courses = courseRepository.findByUserIdWithRelations(userId);
        return ResponseEntity.ok(
                courses.stream()
                        .map(courseMapper::toResponseDto)
                        .toList()
        );
    }

    public String validateFilds(CourseRequestDto courseRequestDto){
        StringBuilder message = new StringBuilder();

        // Validar título
        if (courseRequestDto.getTitle() == null || courseRequestDto.getTitle().isBlank()) {
            message.append("El título no puede estar vacío. ");
        } else if (courseRequestDto.getTitle().length() > 120) {
            message.append("El título no puede tener más de 120 caracteres. ");
        }

        // Validar descripción
        if (courseRequestDto.getDescription() == null || courseRequestDto.getDescription().isBlank()) {
            message.append("La descripción es obligatoria. ");
        } else if (courseRequestDto.getDescription().length() > 500) {
            message.append("La descripción no puede tener más de 500 caracteres. ");
        }

        // Validar categoría
        if (courseRequestDto.getCategoryId() == null) {
            message.append("Debe seleccionar una categoría. ");
        }

        // Validar precio
        if(courseRequestDto.getPrice()!= null) {
            if (courseRequestDto.getPrice().compareTo(BigDecimal.ZERO) < 0) {
                message.append("El precio no puede ser negativo. ");
            }
        }

        // Validar portada
        if (courseRequestDto.getCoverUrl() == null ) {
            message.append("Debe seleccionar una portada. ");
        }
        // Validar semestre recomendado
        if (courseRequestDto.getSemester() == null) {
            message.append("El semestre recomendado es obligatorio. ");
        } else if (courseRequestDto.getSemester() < 0 || courseRequestDto.getSemester() > 10) {
            message.append("El semestre recomendado debe estar entre 0 y 13. ");
        }

        // Validar saberes previos
        if (courseRequestDto.getSemester() != null && courseRequestDto.getSemester() > 0) {
            if (courseRequestDto.getPriorKnowledge() == null || courseRequestDto.getPriorKnowledge().isBlank()) {
                message.append("Los saberes previos son obligatorios si el semestre recomendado es mayor a 0. ");
            } else if (courseRequestDto.getPriorKnowledge().length() > 800) {
                message.append("Los saberes previos no pueden tener más de 800 caracteres. ");
            }
        }

        // Validar profesor
        if (courseRequestDto.getUserId() == null) {
            message.append("Debe indicar un profesor válido. ");
        }
        return message.toString().trim();
    }

    @Override
    public ResponseEntity<?> updateCourse(Long courseId, CourseRequestDto courseRequestDto) throws Exception {
        Optional<Course> optionalCourse = courseRepository.findById(courseId);
        if (optionalCourse.isEmpty()) {
            return ResponseEntity.badRequest().body("El curso con id " + courseId + " no existe.");
        }

        Course course = optionalCourse.get();

        // Validar campos
        String messageValidate = validateFilds(courseRequestDto);
        if (!messageValidate.isEmpty()) {
            return ResponseEntity.badRequest().body(messageValidate);
        }

        // Actualizar campos simples
        course.setTitle(courseRequestDto.getTitle());
        course.setDescription(courseRequestDto.getDescription());
        course.setPrice(courseRequestDto.getPrice());
        course.setSemester(courseRequestDto.getSemester());
        course.setPriorKnowledge(courseRequestDto.getPriorKnowledge());
        course.setEstimatedDurationMinutes(courseRequestDto.getEstimatedDurationMinutes());

        // Actualizar relaciones
        if (courseRequestDto.getCategoryId() != null) {
            Category category = categoryRepository.findById(courseRequestDto.getCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Categoría no encontrada"));
            course.setCategory(category);
        }

        if (courseRequestDto.getUserId() != null) {
            User profesor = userRepository.findById(courseRequestDto.getUserId())
                    .orElseThrow(() -> new IllegalArgumentException("Profesor no encontrado"));
            course.setUser(profesor);
        }

        if (courseRequestDto.getCurrentStatusId() != null) {
            CurrentStatus currentStatus = currentStatusRepository.findById(courseRequestDto.getCurrentStatusId())
                    .orElseThrow(() -> new IllegalArgumentException("Estado actual no encontrado"));
            course.setCurrentStatus(currentStatus);
        }

        if (courseRequestDto.getAuditStatusId() != null) {
            AuditStatus auditStatus = auditStatusRepository.findById(courseRequestDto.getAuditStatusId())
                    .orElseThrow(() -> new IllegalArgumentException("Estado de auditoría no encontrado"));
            course.setAuditStatus(auditStatus);
        }

        // Portada (subir nueva si se envía)
        if (courseRequestDto.getCoverUrl() != null && !courseRequestDto.getCoverUrl().isEmpty()) {
            Map uploadResult = cloudinaryService.uploadFile(courseRequestDto.getCoverUrl());
            String imageUrl = (String) uploadResult.get("secure_url");
            course.setCoverUrl(imageUrl);
        }

        // Guardar cambios
        Course updated = courseRepository.save(course);

        return ResponseEntity.ok(courseMapper.toResponseDto(updated));
    }

    @Override
    public ResponseEntity<?> deleteCourse(Long courseId) throws Exception {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado"));

        Date now = new Date();

        course.setState(EnumState.DELETED);
        course.setDeletedAt(now);
        course.setUpdatedAt(now);

        courseRepository.save(course);

        return ResponseEntity.ok("Curso marcado como eliminado en " + course.getDeletedAt());
    }




}

package com.uniquindio.edu.edusoft.service.impl;

import com.uniquindio.edu.edusoft.config.security.JwtService;
import com.uniquindio.edu.edusoft.model.dto.enrollment.EnrollmentRequestDto;
import com.uniquindio.edu.edusoft.model.dto.enrollment.EnrollmentResponseDto;
import com.uniquindio.edu.edusoft.model.entities.Course;
import com.uniquindio.edu.edusoft.model.entities.User;
import com.uniquindio.edu.edusoft.model.entities.UserCourse;
import com.uniquindio.edu.edusoft.model.enums.EnumUserCourse;
import com.uniquindio.edu.edusoft.model.mapper.EnrollmentMapper;
import com.uniquindio.edu.edusoft.repository.CourseRepository;
import com.uniquindio.edu.edusoft.repository.UserCourseRepository;
import com.uniquindio.edu.edusoft.repository.UserRepository;
import com.uniquindio.edu.edusoft.service.EnrollmentService;
import io.jsonwebtoken.Claims;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EnrollmentServiceImpl implements EnrollmentService {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final UserCourseRepository userCourseRepository;
    private final EnrollmentMapper enrollmentMapper;
    private final JwtService jwtService;

    @Override
    public ResponseEntity<?> enrollToCourse(EnrollmentRequestDto request, Authentication authentication) {
        // Obtener usuario autenticado
        User user = userRepository.findByEmail(authentication.getName().toLowerCase())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        // Buscar curso
        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado"));

        // Verificar si ya está inscrito
        Optional<UserCourse> existingEnrollment = userCourseRepository
                .findByUserIdAndCourseIdAndUserCourse(user.getId(), course.getId(), EnumUserCourse.INSCRITO);

        if (existingEnrollment.isPresent()) {
            return ResponseEntity.badRequest().body("El usuario ya está inscrito en este curso.");
        }

        // Si fue eliminado previamente, reactivar inscripción
        Optional<UserCourse> previouslyDeleted = userCourseRepository
                .findByUserIdAndCourseIdAndUserCourse(user.getId(), course.getId(), EnumUserCourse.ELIMINADO);

        UserCourse saved;
        if (previouslyDeleted.isPresent()) {
            UserCourse reactivated = previouslyDeleted.get();
            reactivated.setUserCourse(EnumUserCourse.INSCRITO);
            reactivated.setEnrollmentDate(LocalDateTime.now());
            reactivated.setCompleted(false);
            reactivated.setProgressPercentage(0.0);
            saved = userCourseRepository.save(reactivated);
        } else {
            //  Crear nueva inscripción
            UserCourse userCourse = new UserCourse();
            userCourse.setUser(user);
            userCourse.setCourse(course);
            userCourse.setEnrollmentDate(LocalDateTime.now());
            userCourse.setCompleted(false);
            userCourse.setProgressPercentage(0.0);
            userCourse.setUserCourse(EnumUserCourse.INSCRITO);
            saved = userCourseRepository.save(userCourse);
        }

        EnrollmentResponseDto response = enrollmentMapper.toResponseDto(saved);
        return ResponseEntity.ok(response);
    }

    @Override
    @Transactional
    public ResponseEntity<?> getCoursesStudent(Authentication authentication) throws Exception {

        log.info("Consultando cursos del estudiante [{}]", authentication.getName());

        User user = userRepository.findByEmail(authentication.getName().toLowerCase())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        List<UserCourse> userCourses = userCourseRepository.findByUserIdAndUserCourse(user.getId(), EnumUserCourse.INSCRITO);
        log.debug("Usuario [{}] tiene [{}] cursos inscritos", user.getId(), userCourses.size());

        // Usamos el mapper en lugar de construir DTOs manualmente
        List<EnrollmentResponseDto> responseList;
        responseList = enrollmentMapper.toResponseDtoList(userCourses);

        log.info("Cursos obtenidos para usuario [{}]: {}", user.getId(), responseList.size());

        return ResponseEntity.ok(responseList);
    }

    @Override
    public ResponseEntity<?> courseUnsubscribe(EnrollmentRequestDto request, Authentication authentication) throws Exception {

        // Usuario autenticado desde la cookie
        User user = userRepository.findByEmail(authentication.getName().toLowerCase())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado"));

        // Buscar la inscripción existente
        Optional<UserCourse> temporary = userCourseRepository.findByUserIdAndCourseIdAndUserCourse(user.getId(), course.getId(),EnumUserCourse.INSCRITO);

        if (temporary.isPresent()) {

            UserCourse userCourse = temporary.get();
            // Actualizar el estado a ELIMINADO
            userCourseRepository.updateState(userCourse.getId(), EnumUserCourse.ELIMINADO.name());
            return ResponseEntity.ok("Se ha cancelado la inscripción del curso correctamente.");

        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("No se encontró inscripción para cancelar.");
        }

    }

    public boolean alreadyEnrolled(Long courseId, Authentication authentication) {

        String email = (String) authentication.getPrincipal();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Optional<UserCourse> existingEnrollment = userCourseRepository
                .findByUserIdAndCourseIdAndUserCourse(user.getId(), courseId, EnumUserCourse.INSCRITO);

        return existingEnrollment.isPresent();
    }
}

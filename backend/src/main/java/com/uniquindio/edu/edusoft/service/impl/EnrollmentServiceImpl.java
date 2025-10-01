package com.uniquindio.edu.edusoft.service.impl;

import com.uniquindio.edu.edusoft.model.dto.enrollment.EnrollmentRequestDto;
import com.uniquindio.edu.edusoft.model.dto.enrollment.EnrollmentResponseDto;
import com.uniquindio.edu.edusoft.model.entities.Course;
import com.uniquindio.edu.edusoft.model.entities.User;
import com.uniquindio.edu.edusoft.model.entities.UserCourse;
import com.uniquindio.edu.edusoft.model.enums.EnumUserCourse;
import com.uniquindio.edu.edusoft.repository.CourseRepository;
import com.uniquindio.edu.edusoft.repository.UserCourseRepository;
import com.uniquindio.edu.edusoft.repository.UserRepository;
import com.uniquindio.edu.edusoft.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class EnrollmentServiceImpl implements EnrollmentService {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    private final UserCourseRepository userCourseRepository;

    @Override
    public ResponseEntity<?> enrollToCourse(EnrollmentRequestDto request, Authentication authentication) throws Exception {
        // Usuario autenticado desde la cookie
        User user = userRepository.findByEmail(authentication.getName().toLowerCase())
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

        Course course = courseRepository.findById(request.getCourseId())
                .orElseThrow(() -> new IllegalArgumentException("Curso no encontrado"));

        // Evitar inscripción duplicada
        if (userCourseRepository.findByUserIdAndCourseId(user.getId(), course.getId()).isPresent()) {
            return ResponseEntity.badRequest().body("El usuario ya está inscrito en este curso");
        }

        // Crear relación usuario-curso
        UserCourse userCourse = new UserCourse();
        userCourse.setUser(user);
        userCourse.setCourse(course);
        userCourse.setEnrollmentDate(LocalDateTime.now());
        userCourse.setCompleted(false);
        userCourse.setProgressPercentage(0.0);
        userCourse.setUserCourse(EnumUserCourse.INSCRITO);

        UserCourse saved = userCourseRepository.save(userCourse);

        EnrollmentResponseDto response = new EnrollmentResponseDto(
                saved.getId(),
                user.getId(),
                user.getName(),
                course.getId(),
                course.getTitle(),
                saved.getEnrollmentDate().toString(),
                saved.getProgressPercentage().intValue(),
                saved.getCompleted(),
                saved.getUserCourse()
        );

        return ResponseEntity.ok(response);
    }
}

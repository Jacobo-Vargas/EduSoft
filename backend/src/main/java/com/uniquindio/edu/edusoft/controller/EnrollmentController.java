package com.uniquindio.edu.edusoft.controller;

import com.uniquindio.edu.edusoft.model.dto.enrollment.EnrollmentRequestDto;
import com.uniquindio.edu.edusoft.service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    @PostMapping("/enroll")
    public ResponseEntity<?> enroll(@RequestBody EnrollmentRequestDto request, Authentication authentication) throws Exception {
        return enrollmentService.enrollToCourse(request, authentication);
    }

    @GetMapping("/finByCoursesUser")
    public ResponseEntity<?> finByCoursesUser(Authentication authentication) throws Exception {
        return enrollmentService.getCoursesStudent(authentication);
    }

    @PutMapping("/courseUnsubscribe")
    public ResponseEntity<?> courseUnsubscribe(@RequestBody EnrollmentRequestDto request, Authentication authentication) throws Exception {
        return enrollmentService.courseUnsubscribe(request, authentication);
    }

}

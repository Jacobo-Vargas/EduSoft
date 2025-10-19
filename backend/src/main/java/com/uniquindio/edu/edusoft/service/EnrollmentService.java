package com.uniquindio.edu.edusoft.service;

import com.uniquindio.edu.edusoft.model.dto.enrollment.EnrollmentRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestBody;

public interface EnrollmentService {
    ResponseEntity<?> enrollToCourse(EnrollmentRequestDto request, Authentication authentication) throws Exception;

    ResponseEntity<?>getCoursesStudent(Authentication authentication)throws Exception;

    public ResponseEntity<?> courseUnsubscribe(@RequestBody EnrollmentRequestDto request, Authentication authentication) throws Exception;

    boolean alreadyEnrolled(Long courseId, Authentication authentication) throws Exception;
}

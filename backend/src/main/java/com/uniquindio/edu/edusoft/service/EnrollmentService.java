package com.uniquindio.edu.edusoft.service;

import com.uniquindio.edu.edusoft.model.dto.enrollment.EnrollmentRequestDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public interface EnrollmentService {
    ResponseEntity<?> enrollToCourse(EnrollmentRequestDto request, Authentication authentication) throws Exception;
}

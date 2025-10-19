package com.uniquindio.edu.edusoft.service;

import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;

public interface PaymentService {

    ResponseEntity<?> getInitPoint(Long courseId, Authentication authentication) throws Exception;
}

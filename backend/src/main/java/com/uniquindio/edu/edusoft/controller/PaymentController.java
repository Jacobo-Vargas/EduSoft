package com.uniquindio.edu.edusoft.controller;

import com.uniquindio.edu.edusoft.model.dto.enrollment.EnrollmentRequestDto;
import com.uniquindio.edu.edusoft.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @GetMapping("/mercado")
    public ResponseEntity<?> mercado(@RequestBody EnrollmentRequestDto request, Authentication authentication) {
        try {
            return paymentService.getInitPoint(request.getCourseId(), authentication);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

}

package com.uniquindio.edu.edusoft.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;


import java.util.Map;

@RestController
@RequestMapping("/api")
public class CaptchaController {

    @Value("${RECAPTCHA_SECRET}")
    private String recaptchaSecret;

    @PostMapping("/validate-captcha")
    public ResponseEntity<?> validateCaptcha(@RequestBody Map<String, String> request) {
        String token = request.get("captcha");
        if (token == null || token.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "No se envió el token"));
        }

        try {
            String verifyUrl = "https://www.google.com/recaptcha/api/siteverify?secret="
                    + recaptchaSecret + "&response=" + token;

            RestTemplate restTemplate = new RestTemplate();
            Map<String, Object> response = restTemplate.postForObject(verifyUrl, null, Map.class);

            if ((Boolean) response.get("success")) {
                return ResponseEntity.ok(Map.of("success", true, "message", "Captcha válido"));
            } else {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Captcha inválido"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(500).body(Map.of("success", false, "message", "Error validando captcha"));
        }
    }
}
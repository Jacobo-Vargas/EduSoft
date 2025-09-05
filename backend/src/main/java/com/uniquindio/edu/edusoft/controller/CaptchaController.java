package com.uniquindio.edu.edusoft.controller;

import jakarta.annotation.security.PermitAll;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;


import java.util.Map;

@RestController
@RequestMapping("/api/captcha")
public class CaptchaController {

    @Value("${RECAPTCHA_SECRET}")
    private String recaptchaSecret;

    @PostMapping("/verify")
    @PermitAll
    public ResponseEntity<?> verifyCaptcha(@RequestBody Map<String, String> request) {
        String token = request.get("token");

        String verifyUrl = "https://www.google.com/recaptcha/api/siteverify?secret="
                + recaptchaSecret + "&response=" + token;

        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> response = restTemplate.postForObject(verifyUrl, null, Map.class);

        if (Boolean.TRUE.equals(response.get("success"))) {
            return ResponseEntity.ok("Captcha válido ✅");
        } else {
            return ResponseEntity.badRequest().body("Captcha inválido ❌");
        }
    }
}

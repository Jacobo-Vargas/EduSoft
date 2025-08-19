package com.uniquindio.edu.edusoft.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api")
public class DemoController {

    @GetMapping("/prueba")
    public ResponseEntity<?> prueba() {
        return null;
    }
}

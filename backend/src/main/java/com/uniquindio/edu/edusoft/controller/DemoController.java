package com.uniquindio.edu.edusoft.controller;

import com.uniquindio.edu.edusoft.config.security.JwtService;
import com.uniquindio.edu.edusoft.utils.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/api")
@RequiredArgsConstructor
public class DemoController {

    private final JwtService jwtService;

    @PostMapping("/prueba")
    public ResponseEntity<?> prueba(@RequestBody() String email) {

        return BaseResponse.response(this.jwtService.generateToken(String.valueOf(email)), "Token  Obtenido con éxito", "success");
    }
}

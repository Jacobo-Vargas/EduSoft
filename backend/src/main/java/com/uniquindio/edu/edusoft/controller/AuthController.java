package com.uniquindio.edu.edusoft.controller;

import com.uniquindio.edu.edusoft.model.dto.respose.AuthResponseDTO;
import com.uniquindio.edu.edusoft.model.dto.respose.LoginRequestDTO;
import com.uniquindio.edu.edusoft.config.security.JwtService;
import com.uniquindio.edu.edusoft.config.security.TokenStoreService;
import com.uniquindio.edu.edusoft.service.LoginService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtService jwtService;
    private final TokenStoreService tokenStoreService;
    private final LoginService loginService;

    public AuthController(JwtService jwtService, TokenStoreService tokenStoreService, LoginService loginService) {
        this.jwtService = jwtService;
        this.tokenStoreService = tokenStoreService;
        this.loginService = loginService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequestdto) throws Exception {
        return loginService.login(loginRequestdto) ;
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        String jti = jwtService.extractJti(token);

        // 1. Eliminar el tokken de Redis
        tokenStoreService.removeToken(jti);

        return ResponseEntity.ok("Sesión cerrada exitosamente");
    }

}

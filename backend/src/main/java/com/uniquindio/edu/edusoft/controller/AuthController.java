package com.uniquindio.edu.edusoft.controller;

import com.uniquindio.edu.edusoft.model.dto.respose.AuthResponseDTO;
import com.uniquindio.edu.edusoft.model.dto.respose.LoginRequestDTO;
import com.uniquindio.edu.edusoft.config.security.JwtService;
import com.uniquindio.edu.edusoft.config.security.TokenStoreService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtService jwtService;
    private final TokenStoreService tokenStoreService;

    public AuthController(JwtService jwtService, TokenStoreService tokenStoreService) {
        this.jwtService = jwtService;
        this.tokenStoreService = tokenStoreService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginRequestDTO request) {
        if ("admin".equals(request.getUsername()) && "1234".equals(request.getPassword())) {


            // 1. Genear token
            String accessToken = jwtService.generateToken(request.getUsername());

            // 1.1 extraer jti
            String jti = jwtService.extractJti(accessToken);

            // 2. Guardar token en Redis con TTL
            tokenStoreService.storeToken(jti, request.getUsername());

            // 3. Retornar token
            return ResponseEntity.ok(new AuthResponseDTO(accessToken));
        }
        return ResponseEntity.status(401).build();
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.substring(7);
        String jti = jwtService.extractJti(token);

        // 1. Eliminar el tokken de Redis
        tokenStoreService.removeToken(jti);

        return ResponseEntity.ok().build();
    }

}

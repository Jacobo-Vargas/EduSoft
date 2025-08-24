package com.uniquindio.edu.edusoft.controller;

import com.uniquindio.edu.edusoft.model.dto.respose.LoginRequestDTO;
import com.uniquindio.edu.edusoft.config.security.JwtService;
import com.uniquindio.edu.edusoft.config.security.TokenStoreService;
import com.uniquindio.edu.edusoft.service.LoginService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final LoginService loginService;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequestdto, HttpServletResponse response) throws Exception {
        return this.loginService.login(loginRequestdto, response) ;
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@CookieValue(name = "accessToken", required = false) String token, HttpServletResponse response) throws Exception {
        return this.loginService.logout(token, response);
    }


}

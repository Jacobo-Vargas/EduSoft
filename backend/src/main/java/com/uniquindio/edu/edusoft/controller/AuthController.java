package com.uniquindio.edu.edusoft.controller;

import com.uniquindio.edu.edusoft.model.dto.respose.LoginRequestDTO;
import com.uniquindio.edu.edusoft.service.LoginService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/sendCodeEmail")
    public ResponseEntity<?> sendCodeEmail(@RequestBody LoginRequestDTO loginRequestdto,HttpServletResponse response) throws Exception {
        return this.loginService.sendCodeEmail(loginRequestdto,response);
    }

    @PostMapping("/updatePassword")
    public ResponseEntity<?> updatePassword(@RequestBody LoginRequestDTO loginRequestdto,HttpServletResponse response) throws Exception {
        return this.loginService.updatePassword(loginRequestdto);
    }


}

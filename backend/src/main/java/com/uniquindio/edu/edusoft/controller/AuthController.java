package com.uniquindio.edu.edusoft.controller;

import com.uniquindio.edu.edusoft.model.dto.respose.LoginRequestDTO;
import com.uniquindio.edu.edusoft.service.LoginService;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AuthController {


    private final LoginService loginService;


    @PermitAll
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequestdto, HttpServletResponse response) throws Exception {
        return this.loginService.login(loginRequestdto, response) ;
    }

    @PostMapping("/logout")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> logout(@CookieValue(name = "accessToken", required = false) String token, HttpServletResponse response) throws Exception {
        return this.loginService.logout(token, response);
    }

    @PostMapping("/sendCodeEmail")
    @PermitAll
    public ResponseEntity<?> sendCodeEmail(@RequestBody LoginRequestDTO loginRequestdto,HttpServletResponse response) throws Exception {
        return this.loginService.sendCodeEmail(loginRequestdto,response);
    }

    @PermitAll
    @PostMapping("/updatePassword")
    public ResponseEntity<?> updatePassword(@RequestBody LoginRequestDTO loginRequestdto,HttpServletResponse response) throws Exception {
        return this.loginService.updatePassword(loginRequestdto);
    }

    @PermitAll
    @PostMapping("/verifyCode/{code}")
    public ResponseEntity<?> verifyCode(@PathVariable String code, @RequestBody LoginRequestDTO loginRequestdto, HttpServletResponse response) throws Exception {
        return this.loginService.verifyCode(code,loginRequestdto);
    }

}

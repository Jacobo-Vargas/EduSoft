package com.uniquindio.edu.edusoft.controller;

import com.uniquindio.edu.edusoft.model.dto.respose.LoginRequestDTO;
import com.uniquindio.edu.edusoft.model.entities.User;
import com.uniquindio.edu.edusoft.repository.LoginRepository;
import com.uniquindio.edu.edusoft.service.LoginService;
import com.uniquindio.edu.edusoft.utils.BaseResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final LoginService loginService;
    private final LoginRepository loginRepository;


    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequestdto, HttpServletResponse response) throws Exception {
        return this.loginService.login(loginRequestdto, response) ;
    }

    @GetMapping("/me")
    public ResponseEntity<?> me(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return BaseResponse.response(null, "Sesion expirada", "succes", HttpStatus.UNAUTHORIZED);
        }


        User user = null;
        user = loginRepository.findByEmail(authentication.getName().toLowerCase())
                    .orElseThrow(() -> new BadCredentialsException("Credenciales inválidas"));


        Map<String, Object> userData = new HashMap<>();
        userData.put("id", user.getId());
        userData.put("userType", user.getUserType().toString());
        userData.put("email", user.getEmail());
        userData.put("name", user.getName());

        return BaseResponse.response(userData, "Sesion activa", "succes", HttpStatus.OK);
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

    @PostMapping("/verifyCode/{code}")
    public ResponseEntity<?> verifyCode(@PathVariable String code, @RequestBody LoginRequestDTO loginRequestdto, HttpServletResponse response) throws Exception {
        return this.loginService.verifyCode(code,loginRequestdto);
    }

}

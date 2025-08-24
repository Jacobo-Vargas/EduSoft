package com.uniquindio.edu.edusoft.service;

import com.uniquindio.edu.edusoft.model.dto.respose.LoginRequestDTO ;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;

public interface LoginService {
    ResponseEntity<?>login(LoginRequestDTO loginRequestdto, HttpServletResponse response) throws Exception;

    ResponseEntity<?>logout(String token, HttpServletResponse response) throws Exception;
}
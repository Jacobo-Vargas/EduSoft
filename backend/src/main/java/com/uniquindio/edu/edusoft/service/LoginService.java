package com.uniquindio.edu.edusoft.service;

import com.uniquindio.edu.edusoft.model.dto.respose.LoginRequestDTO ;
import org.springframework.http.ResponseEntity;

public interface LoginService {
    ResponseEntity<?>login(LoginRequestDTO loginRequestdto) throws Exception;
}
package com.uniquindio.edu.edusoft.service;

import com.uniquindio.edu.edusoft.model.dto.respose.ResponseDTO;
import com.uniquindio.edu.edusoft.model.dto.user.RequestUserDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public interface UserService {

    ResponseEntity<ResponseDTO> createUser(RequestUserDTO requestUserDTO) throws Exception;

    ResponseEntity<ResponseDTO> sendCodeConfirmation(String email) throws Exception;

    ResponseEntity<ResponseDTO> verifyAccountEmailCode(String email, String code) throws Exception;

    ResponseEntity<ResponseDTO> verifyUserByToken(String token) throws Exception;

    ResponseEntity<?> userInformation(Authentication authentication) throws  Exception;
}

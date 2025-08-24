package com.uniquindio.edu.edusoft.service;

import com.uniquindio.edu.edusoft.model.dto.respose.ResponseDTO;
import com.uniquindio.edu.edusoft.model.dto.user.RequestUserDTO;
import org.springframework.http.ResponseEntity;

public interface UserService {

    ResponseEntity<ResponseDTO> createUser(RequestUserDTO requestUserDTO) throws Exception;

    ResponseEntity<ResponseDTO> sendCodeConfirmation(String email) throws Exception;

    ResponseEntity<ResponseDTO> verifyAccountEmailCode(String email, String code) throws Exception;

}

package com.uniquindio.edu.edusoft.service;

import com.uniquindio.edu.edusoft.model.dto.user.CreateUserDTO;
import com.uniquindio.edu.edusoft.model.dto.user.VerifyAccountEmailCodeDTO;
import org.springframework.http.ResponseEntity;

public interface UserService {

    ResponseEntity<?> createUser(CreateUserDTO createUserDTO)throws Exception;
    ResponseEntity<?> sendCodeConfirmation(String email) throws Exception;
    ResponseEntity<?> verifyAccountEmailCode(VerifyAccountEmailCodeDTO verifyAccountEmailCodeDto) throws  Exception;

}

package com.uniquindio.edu.edusoft.service;

import com.uniquindio.edu.edusoft.model.DTO.user.CreateUserDTO;
import com.uniquindio.edu.edusoft.model.DTO.user.VerifyAccountEmailCodeDto;
import org.springframework.http.ResponseEntity;

public interface UserService {

    ResponseEntity<?> createUser(CreateUserDTO createUserDTO)throws Exception;
    ResponseEntity<?> sendCodeConfirmation(String email) throws Exception;
    ResponseEntity<?> verifyAccountEmailCode(VerifyAccountEmailCodeDto verifyAccountEmailCodeDto) throws  Exception;

}

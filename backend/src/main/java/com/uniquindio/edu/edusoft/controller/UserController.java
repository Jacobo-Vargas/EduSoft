package com.uniquindio.edu.edusoft.controller;

import com.uniquindio.edu.edusoft.model.DTO.user.CreateUserDTO;
import com.uniquindio.edu.edusoft.model.DTO.user.VerifyAccountEmailCodeDto;
import com.uniquindio.edu.edusoft.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    //enviar codigo verificaion
    @PostMapping("/sendCodeConfirmation/{email}")
    public ResponseEntity<?> sendCodeConfirmation(@PathVariable String email) throws Exception {
        return userService.sendCodeConfirmation(email);
    }
    //crear usuario
    @PostMapping("/createUser/")
    public ResponseEntity<?> createUser(@RequestBody @Valid CreateUserDTO createUserDTO) throws Exception {
        return userService.createUser(createUserDTO);
    }
    //verificar cuenta
    @PutMapping("/verifyAccountEmailCode")
    ResponseEntity<?> verifyAccountEmailCode(@RequestBody @Valid VerifyAccountEmailCodeDto verifyAccountEmailCodeDto) throws  Exception {
        return userService.verifyAccountEmailCode(verifyAccountEmailCodeDto);
    }

}

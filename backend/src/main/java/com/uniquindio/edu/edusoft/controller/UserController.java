package com.uniquindio.edu.edusoft.controller;

import com.uniquindio.edu.edusoft.model.dto.respose.ResponseDTO;
import com.uniquindio.edu.edusoft.model.dto.user.RequestUserDTO;
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

    // Crear usuario
    @PostMapping("/createUser")
    public ResponseEntity<ResponseDTO> createUser(@RequestBody @Valid RequestUserDTO requestUserDTO) throws Exception {
        return userService.createUser(requestUserDTO);
    }

    // Enviar código de verificación
    @PostMapping("/sendCodeConfirmation/{email}")
    public ResponseEntity<ResponseDTO> sendCodeConfirmation(@PathVariable String email) throws Exception {
        return userService.sendCodeConfirmation(email);
    }

    // Verificar cuenta con código
    @PutMapping("/verifyAccountEmailCode/{email}/{code}")
    public ResponseEntity<ResponseDTO> verifyAccountEmailCode(
            @PathVariable String email,
            @PathVariable String code) throws Exception {
        return userService.verifyAccountEmailCode(email, code);
    }
}

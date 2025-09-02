package com.uniquindio.edu.edusoft.controller;

import com.uniquindio.edu.edusoft.model.dto.respose.ResponseDTO;
import com.uniquindio.edu.edusoft.model.dto.user.RequestUserDTO;
import com.uniquindio.edu.edusoft.service.UserService;
import com.uniquindio.edu.edusoft.utils.HtmlTemplates;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    // Crear usuario
    @PostMapping("/createUser")
    public ResponseEntity<ResponseDTO> createUser(@RequestBody @Valid RequestUserDTO requestUserDTO) throws Exception {
        log.info("UserController:createUser");
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

    // Verificar usuario por token (desde el correo)
    @GetMapping("/verify")
    public ResponseEntity<String> verifyUser(@RequestParam("token") String token) {
        try {
            userService.verifyUserByToken(token);

            return ResponseEntity.ok()
                    .header("Content-Type", "text/html; charset=UTF-8")
                    .body(HtmlTemplates.verificationSuccessPage());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header("Content-Type", "text/html; charset=UTF-8")
                    .body(HtmlTemplates.verificationErrorPage());
        }
    }
}

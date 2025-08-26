package com.uniquindio.edu.edusoft.controller;

import com.uniquindio.edu.edusoft.model.dto.respose.ResponseDTO;
import com.uniquindio.edu.edusoft.model.dto.user.RequestUserDTO;
import com.uniquindio.edu.edusoft.service.UserService;
import com.uniquindio.edu.edusoft.utils.HtmlPageBuilder;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final HtmlPageBuilder htmlPageBuilder;

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

    // Verificar cuenta con token (devuelve HTML)
    @GetMapping("/verify")
    public ResponseEntity<String> verifyUser(@RequestParam("token") String token) {
        try {
            ResponseEntity<ResponseDTO> result = userService.verifyUserByToken(token);

            if (result.getStatusCode().is2xxSuccessful()) {
                // Página de éxito
                String successPage = htmlPageBuilder.buildSuccessPage();
                return ResponseEntity.ok()
                        .header("Content-Type", "text/html; charset=UTF-8")
                        .body(successPage);
            } else {
                // Página de error
                String errorPage = htmlPageBuilder.buildErrorPage("Token inválido o expirado");
                return ResponseEntity.badRequest()
                        .header("Content-Type", "text/html; charset=UTF-8")
                        .body(errorPage);
            }
        } catch (Exception e) {
            // Página de error del servidor
            String errorPage = htmlPageBuilder.buildErrorPage("Error al verificar la cuenta");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .header("Content-Type", "text/html; charset=UTF-8")
                    .body(errorPage);
        }
    }



}
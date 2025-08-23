package com.uniquindio.edu.edusoft.model.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record VerifyAccountEmailCodeDTO(
        @Email
        String email,


        @NotBlank(message = "El codigo de confirmacion es obligatorio")
        String code
) {
}

package com.uniquindio.edu.edusoft.model.DTO.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record VerifyAccountEmailCodeDto(
        @Email
        String email,


        @NotBlank(message = "El codigo de confirmacion es obligatorio")
        String code
) {
}

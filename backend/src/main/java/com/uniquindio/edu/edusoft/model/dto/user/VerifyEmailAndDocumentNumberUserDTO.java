package com.uniquindio.edu.edusoft.model.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record VerifyEmailAndDocumentNumberUserDTO(

        @Email
        String email,
        @NotBlank
        String documentNumber

) {
}

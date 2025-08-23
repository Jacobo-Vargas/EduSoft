package com.uniquindio.edu.edusoft.model.dto.respose;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record VerifyEmailAndDocumentNumberUserDto(

        @Email
        String email,
        @NotBlank
        String documentNumber

) {
}

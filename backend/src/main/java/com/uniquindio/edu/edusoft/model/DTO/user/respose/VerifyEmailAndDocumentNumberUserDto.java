package com.uniquindio.edu.edusoft.model.DTO.user.respose;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record VerifyEmailAndDocumentNumberUserDto(

        @Email
        String email,
        @NotBlank
        String documentNumber

) {
}

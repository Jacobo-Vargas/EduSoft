package com.uniquindio.edu.edusoft.model.DTO.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ChangeUserPassword(

        @NotBlank(message = "EL documento de identificación  es obligatorio para poder cambiar la clave del usuario")
        String documentNumber,
        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 4, max = 20, message = "La contraseña debe tener entre 8 y 20 caracteres")
        @Pattern(
                regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
                message = "La contraseña debe contener al menos una mayúscula, una minúscula, un número y un carácter especial"
        )
        String password

) {
}

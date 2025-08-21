package com.uniquindio.edu.edusoft.model.DTO.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Email;
import org.hibernate.validator.constraints.Length;

public record CreateUserDTO(

        @NotBlank(message = "Debe registrar el número de documento")
        String documentNumber,

        @NotBlank(message = "Debe registrar al menos un nombre")
        @Length(max = 50)
        String name,

        @NotBlank(message = "El número de celular es obligatorio")
        @Pattern(regexp = "^\\d{10}$", message = "El número de celular debe tener 10 dígitos")
        String phone,

        @NotBlank(message = "La dirección es obligatoria")
        @Length(max = 100)
        String address,

        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El email debe tener un formato válido")
        String email,

        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 8, max = 20, message = "La contraseña debe tener entre 8 y 20 caracteres")
        @Pattern(
                regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@$!%*?&]).+$",
                message = "La contraseña debe contener al menos una mayúscula, una minúscula, un número y un carácter especial"
        )
        String password
) {}
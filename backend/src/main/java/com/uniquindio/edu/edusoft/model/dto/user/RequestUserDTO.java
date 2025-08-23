package com.uniquindio.edu.edusoft.model.dto.user;

import com.uniquindio.edu.edusoft.model.enums.EnumUserType;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestUserDTO {

    @NotBlank(message = "Debe registrar el número de documento")
    private String documentNumber;

    @NotBlank(message = "Debe registrar al menos un nombre")
    private String name;

    @NotBlank(message = "El número de celular es obligatorio")
    @Pattern(regexp = "^\\d{10}$", message = "El número de celular debe tener 10 dígitos")
    private String phone;

    @NotBlank(message = "La dirección es obligatoria")
    private String address;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email debe tener un formato válido")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, max = 20, message = "La contraseña debe tener entre 8 y 20 caracteres")
    private String password;

    private EnumUserType userType;
}

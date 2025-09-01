package com.uniquindio.edu.edusoft.model.dto.module;

import com.uniquindio.edu.edusoft.model.enums.EnumLifecycleStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ModuleRequestDto {

    @NotBlank(message = "El nombre del módulo es obligatorio")
    @Size(max = 120, message = "El nombre no puede exceder 120 caracteres")
    private String name;

    @Size(max = 800, message = "La descripción no puede exceder 800 caracteres")
    private String description;

    @NotNull(message = "Debe especificar el curso")
    private Long courseId;

    @NotNull(message = "El orden es obligatorio")
    private Integer displayOrder;

    private EnumLifecycleStatus lifecycleStatus = EnumLifecycleStatus.BORRADOR;

    private Boolean isVisible = true;
}
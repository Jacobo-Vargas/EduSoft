package com.uniquindio.edu.edusoft.model.dto.lesson;

import com.uniquindio.edu.edusoft.model.enums.EnumLifecycleStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LessonRequestDto {

    @NotBlank(message = "El nombre de la lección es obligatorio")
    @Size(max = 120, message = "El nombre no puede exceder 120 caracteres")
    private String name;

    @Size(max = 1000, message = "La descripción no puede exceder 1000 caracteres")
    private String description;

    @NotNull(message = "Debe especificar el módulo")
    private Long moduleId;

    @NotNull(message = "El orden es obligatorio")
    private Integer displayOrder;

    private EnumLifecycleStatus lifecycleStatus = EnumLifecycleStatus.BORRADOR;

    private Boolean isVisible = false;
}
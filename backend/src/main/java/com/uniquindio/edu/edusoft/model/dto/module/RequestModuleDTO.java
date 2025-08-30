package com.uniquindio.edu.edusoft.model.dto.module;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestModuleDTO {
    @NotBlank(message = "El título del módulo es obligatorio")
    private String title;
    private String description;
    private int orderNumber;
    private boolean visible;
    private Long courseId;
}

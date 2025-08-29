package com.uniquindio.edu.edusoft.model.dto.lesson;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestLessonDTO {

    @NotBlank(message = "El título de la lección es obligatorio")
    private String title;

    @NotBlank(message = "El contenido de la lección es obligatorio")
    private String content;

    private int orderNumber;
    private int durationMinutes;

    private Long moduleId; // referencia al módulo al que pertenece
}

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
    private String description;
    private int orderNumber;
    private int durationMinutes;
    private boolean visible;
    private Long moduleId;
}

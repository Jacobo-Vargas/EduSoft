package com.uniquindio.edu.edusoft.model.dto.content;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class ContentAssignmentDto {

    @NotNull(message = "El ID de la lección es obligatorio")
    private Long lessonId;

    @NotEmpty(message = "La lista de contenidos no puede estar vacía")
    private List<ContentOrderDto> contents;

    @Data
    public static class ContentOrderDto {
        @NotNull(message = "El ID del contenido es obligatorio")
        private Long contentId;

        @NotNull(message = "El orden es obligatorio")
        private Integer displayOrder;
    }
}
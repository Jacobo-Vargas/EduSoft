package com.uniquindio.edu.edusoft.model.dto.document;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestDocumentDTO {
    @NotBlank(message = "El nombre del documento es obligatorio")
    private String name;
    @NotBlank(message = "La URL del documento es obligatoria")
    private String url;
    private int orderNumber;
    private Long lessonId;
}


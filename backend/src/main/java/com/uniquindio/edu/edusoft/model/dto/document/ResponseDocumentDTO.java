package com.uniquindio.edu.edusoft.model.dto.document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseDocumentDTO {
    private Long id;
    private String name;
    private String url;
    private int orderNumber;
    private Long lessonId;
    private String lessonTitle;
}


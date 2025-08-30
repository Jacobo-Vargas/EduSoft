package com.uniquindio.edu.edusoft.model.dto.lesson;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseLessonDTO {
    private Long id;
    private String title;
    private String description;
    private int orderNumber;
    private int durationMinutes;
    private boolean visible;
    private String status;
    private Long moduleId;
    private String moduleTitle;
}


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
    private String content;
    private int orderNumber;
    private int durationMinutes;
    private Long moduleId;
    private String moduleTitle;
}

package com.uniquindio.edu.edusoft.model.dto.history;
import lombok.Data;

@Data
public class CourseEventResponseDto {
    private Long id;
    private String eventType;
    private String description;
    private String createdAt;
    private String createdBy;
    private Long courseId;
    private Long moduleId;
    private Long lessonId;
    private Long contentId;
}

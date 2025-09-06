package com.uniquindio.edu.edusoft.model.dto.content;

import lombok.Data;

@Data
public class ContentResponseDto {
    private Long id;
    private String title;
    private String description;
    private String fileUrl;
    private String fileType;
    private Long lessonId;
    private String lessonName;
    private Long courseId;
    private String courseName;
    private Integer displayOrder;
    private Boolean isVisible;
    private String createdAt;
    private String updatedAt;
}


package com.uniquindio.edu.edusoft.model.dto.content;

import com.uniquindio.edu.edusoft.model.enums.EnumLifecycleStatus;
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
    private EnumLifecycleStatus lifecycleStatus;
    private Boolean isVisible;
    private String createdAt;
    private String updatedAt;
}


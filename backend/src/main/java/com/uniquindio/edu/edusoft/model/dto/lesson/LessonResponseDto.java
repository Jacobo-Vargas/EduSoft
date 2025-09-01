package com.uniquindio.edu.edusoft.model.dto.lesson;

import com.uniquindio.edu.edusoft.model.enums.EnumLifecycleStatus;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class LessonResponseDto {
    private Long id;
    private String name;
    private String description;
    private Long moduleId;
    private String moduleName;
    private Long courseId;
    private String courseName;
    private Integer displayOrder;
    private EnumLifecycleStatus lifecycleStatus;
    private Boolean isVisible;
    private Integer contentsCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
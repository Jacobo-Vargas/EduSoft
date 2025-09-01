package com.uniquindio.edu.edusoft.model.dto.module;

import com.uniquindio.edu.edusoft.model.enums.EnumLifecycleStatus;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class ModuleResponseDto {
    private Long id;
    private String name;
    private String description;
    private Long courseId;
    private String courseName;
    private Integer displayOrder;
    private EnumLifecycleStatus lifecycleStatus;
    private Boolean isVisible;
    private Integer lessonsCount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
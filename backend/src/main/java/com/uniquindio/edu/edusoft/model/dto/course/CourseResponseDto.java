package com.uniquindio.edu.edusoft.model.dto.course;

import com.uniquindio.edu.edusoft.model.entities.AuditStatus;
import com.uniquindio.edu.edusoft.model.entities.Category;
import com.uniquindio.edu.edusoft.model.entities.CurrentStatus;
import com.uniquindio.edu.edusoft.model.entities.User;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CourseResponseDto {
    private Long id;
    private String title;
    private String description;
    private BigDecimal price;
    private String coverUrl;
    private Integer semester;
    private String priorKnowledge;
    private Integer estimatedDurationMinutes;

    // Relaciones enriquecidas
    private Long categoryId;
    private String categoryName;

    private Long currentStatusId;
    private String currentStatusName;

    private Long auditStatusId;
    private String auditStatusName;

    private String userId;   // si tu User usa String
    private String userName;

    // Fechas
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

package com.uniquindio.edu.edusoft.model.dto.course;

import com.uniquindio.edu.edusoft.model.entities.AuditStatus;
import com.uniquindio.edu.edusoft.model.entities.Category;
import com.uniquindio.edu.edusoft.model.entities.CurrentStatus;
import com.uniquindio.edu.edusoft.model.entities.User;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CourseRequestDto {

    private String title;
    private String description;
    private BigDecimal price;
    private String coverUrl;
    private Integer semester;
    private String priorKnowledge;
    private Integer estimatedDurationMinutes;
    // Relacionados solo por ID
    private Long categoryId;
    private Long currentStatusId;
    private Long auditStatusId;
    private String userId;
}

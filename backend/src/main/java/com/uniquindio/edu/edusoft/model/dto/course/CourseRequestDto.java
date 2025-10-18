package com.uniquindio.edu.edusoft.model.dto.course;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Data
public class CourseRequestDto {

    private String title;
    private String description;
    private BigDecimal price;
    private MultipartFile coverUrl;
    private Integer semester;
    private String priorKnowledge;
    private Integer estimatedDurationMinutes;
    private Long categoryId;
    private Long currentStatusId;
    private Long auditStatusId;
    private String userId;
    private String state;
    private Boolean isVisible = false;
}

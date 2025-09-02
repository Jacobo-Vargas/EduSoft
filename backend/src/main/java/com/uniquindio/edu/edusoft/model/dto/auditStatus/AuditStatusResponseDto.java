package com.uniquindio.edu.edusoft.model.dto.auditStatus;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AuditStatusResponseDto {
    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

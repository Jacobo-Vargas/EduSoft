package com.uniquindio.edu.edusoft.model.dto.CurrentStatus;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CurrentStatusResponseDto {

    private Long id;
    private String name;
    private String description;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}

package com.uniquindio.edu.edusoft.model.dto.content;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class ContentAssignmentDto {
    @NotNull
    private Long lessonId;

    private List<ContentOrderDto> contents;

    @Data
    public static class ContentOrderDto {
        private Long contentId;
        private Integer displayOrder;
    }
}

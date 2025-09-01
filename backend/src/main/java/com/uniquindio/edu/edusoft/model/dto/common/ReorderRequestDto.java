package com.uniquindio.edu.edusoft.model.dto.common;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.util.List;

@Data
public class ReorderRequestDto {

    @NotEmpty(message = "La lista de elementos no puede estar vacía")
    private List<ReorderItemDto> items;

    @Data
    public static class ReorderItemDto {
        @NotNull(message = "El ID es obligatorio")
        private Long id;

        @NotNull(message = "El nuevo orden es obligatorio")
        private Integer newOrder;
    }
}
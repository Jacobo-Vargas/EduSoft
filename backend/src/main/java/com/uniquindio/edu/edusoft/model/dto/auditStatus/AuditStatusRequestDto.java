package com.uniquindio.edu.edusoft.model.dto.auditStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AuditStatusRequestDto {
    @NotBlank
    @Size(max = 120)
    private String name;

    @NotBlank
    @Size(max = 500)
    private String description;
}

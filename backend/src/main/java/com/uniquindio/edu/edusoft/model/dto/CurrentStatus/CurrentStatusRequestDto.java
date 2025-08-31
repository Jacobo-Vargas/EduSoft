package com.uniquindio.edu.edusoft.model.dto.CurrentStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CurrentStatusRequestDto {

    @NotBlank
    @Size(max = 120)
    private String name;

    @Size(max = 500)
    private String description;
}
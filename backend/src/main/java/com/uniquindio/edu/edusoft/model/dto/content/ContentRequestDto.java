package com.uniquindio.edu.edusoft.model.dto.content;

import com.uniquindio.edu.edusoft.model.enums.EnumLifecycleStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ContentRequestDto {

    @NotBlank
    @Size(max = 200)
    private String title;

    @Size(max = 500)
    private String description;

    @NotBlank
    @Size(max = 1000)
    private String fileUrl;

    @NotNull
    private Long lessonId;

    @NotNull
    private Integer displayOrder;

    private Boolean isVisible = true;

    private EnumLifecycleStatus lifecycleStatus = EnumLifecycleStatus.BORRADOR;
}

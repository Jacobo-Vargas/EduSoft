package com.uniquindio.edu.edusoft.model.dto.course;

import com.uniquindio.edu.edusoft.model.enums.EnumCourseCategoty;
import com.uniquindio.edu.edusoft.model.enums.EnumCourseLevel;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RequestCourseDTO {

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotNull
    private EnumCourseCategoty category;

    @NotNull
    private EnumCourseLevel level;

    @NotNull
    private int estimatedDurationMinutes;

    private String imageUrl;
}

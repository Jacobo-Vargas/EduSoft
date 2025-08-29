package com.uniquindio.edu.edusoft.model.dto.course;

import com.uniquindio.edu.edusoft.model.enums.EnumCourseCategoty;
import com.uniquindio.edu.edusoft.model.enums.EnumCourseLevel;
import com.uniquindio.edu.edusoft.model.enums.EnumCourseType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResponseCourseDTO {

    private Long id;
    private String title;
    private String description;

    private EnumCourseCategoty category;
    private EnumCourseLevel level;
    private EnumCourseType status;

    private int estimatedDurationMinutes;
    private String imageUrl;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String authorName;
}

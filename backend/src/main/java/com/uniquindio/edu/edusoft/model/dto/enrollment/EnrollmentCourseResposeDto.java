package com.uniquindio.edu.edusoft.model.dto.enrollment;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentCourseResposeDto {

    private Long id;
    private String title;
    private String description;
    private String coverUrl;
    private Integer estimatedDurationMinutes;
    private LocalDateTime createdAt;

}

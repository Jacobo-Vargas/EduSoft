package com.uniquindio.edu.edusoft.model.dto.enrollment;

import com.uniquindio.edu.edusoft.model.enums.EnumUserCourse;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EnrollmentResponseDto {
    private Long id;
    private Long userId;
    private String userName;
    private Long courseId;
    private String courseTitle;
    private String enrollmentDate;
    private Integer progressPercentage;
    private Boolean isCompleted;
    private EnumUserCourse userCourse;
}

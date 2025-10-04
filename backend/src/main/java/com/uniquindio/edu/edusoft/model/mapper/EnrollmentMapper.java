package com.uniquindio.edu.edusoft.model.mapper;

import com.uniquindio.edu.edusoft.model.dto.enrollment.EnrollmentCourseResposeDto;
import com.uniquindio.edu.edusoft.model.dto.enrollment.EnrollmentResponseDto;
import com.uniquindio.edu.edusoft.model.entities.Course;
import com.uniquindio.edu.edusoft.model.entities.UserCourse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EnrollmentMapper {

    // Convierte la entidad principal UserCourse → DTO principal
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.name", target = "userName")
    @Mapping(source = "course", target = "course")
    @Mapping(source = "course.title", target = "courseTitle")
    @Mapping(source = "completed", target = "isCompleted")
    @Mapping(source = "progressPercentage", target = "progressPercentage")
    @Mapping(source = "userCourse", target = "userCourse")
    @Mapping(source = "enrollmentDate", target = "enrollmentDate", dateFormat = "yyyy-MM-dd")
    EnrollmentResponseDto toResponseDto(UserCourse userCourse);

    // Convierte la entidad Course → DTO de Course
    EnrollmentCourseResposeDto toCourseDto(Course course);

    // Lista de inscripciones
    List<EnrollmentResponseDto> toResponseDtoList(List<UserCourse> userCourses);
}

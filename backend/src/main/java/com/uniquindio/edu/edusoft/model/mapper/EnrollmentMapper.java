package com.uniquindio.edu.edusoft.model.mapper;

import com.uniquindio.edu.edusoft.model.dto.enrollment.EnrollmentResponseDto;
import com.uniquindio.edu.edusoft.model.entities.UserCourse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EnrollmentMapper {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.name", target = "userName")
    @Mapping(source = "course.id", target = "courseId")
    @Mapping(source = "course.title", target = "courseTitle")
    @Mapping(source = "completed", target = "isCompleted")
    @Mapping(source = "progressPercentage", target = "progressPercentage")
    @Mapping(source = "userCourse", target = "userCourse")
    EnrollmentResponseDto toResponseDto(UserCourse userCourse);

    List<EnrollmentResponseDto> toResponseDtoList(List<UserCourse> userCourses);
}

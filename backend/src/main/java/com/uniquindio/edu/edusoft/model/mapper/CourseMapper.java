package com.uniquindio.edu.edusoft.model.mapper;

import com.uniquindio.edu.edusoft.model.dto.course.RequestCourseDTO;
import com.uniquindio.edu.edusoft.model.dto.course.ResponseCourseDTO;
import com.uniquindio.edu.edusoft.model.entities.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    /*Course toEntity(RequestCourseDTO requestCourseDTO);

    @Mapping(source = "author.fullName", target = "authorName")
    ResponseCourseDTO toResponseDTO(Course course);

    List<ResponseCourseDTO> toResponseDTOList(List<Course> courses);*/
}

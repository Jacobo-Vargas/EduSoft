package com.uniquindio.edu.edusoft.model.mapper;

import com.uniquindio.edu.edusoft.model.dto.course.CourseRequestDto;
import com.uniquindio.edu.edusoft.model.dto.course.CourseResponseDto;
import com.uniquindio.edu.edusoft.model.dto.course.RequestCourseDTO;
import com.uniquindio.edu.edusoft.model.dto.course.ResponseCourseDTO;
import com.uniquindio.edu.edusoft.model.entities.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CourseMapper {


    CourseMapper INSTANCE = Mappers.getMapper(CourseMapper.class);

    // -------- Entity → Response DTO --------
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    @Mapping(source = "currentStatus.id", target = "currentStatusId")
    @Mapping(source = "currentStatus.name", target = "currentStatusName")
    @Mapping(source = "auditStatus.id", target = "auditStatusId")
    @Mapping(source = "auditStatus.name", target = "auditStatusName")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.name", target = "userName") // suponiendo que tu entidad User tiene "name"
    CourseResponseDto toResponseDto(Course course);

    List<CourseResponseDto> toResponseDtoList(List<Course> courses);

    // DTO → Entity (ojo: relaciones deben cargarse aparte con repositorios)
    @Mapping(source = "categoryId", target = "category.id")
    @Mapping(source = "currentStatusId", target = "currentStatus.id")
    @Mapping(source = "auditStatusId", target = "auditStatus.id")
    @Mapping(source = "userId", target = "user.id")
    Course toEntity(CourseRequestDto dto);

}

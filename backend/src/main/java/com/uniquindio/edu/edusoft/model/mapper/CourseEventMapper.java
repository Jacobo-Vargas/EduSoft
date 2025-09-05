package com.uniquindio.edu.edusoft.model.mapper;


import com.uniquindio.edu.edusoft.model.dto.history.CourseEventResponseDto;
import com.uniquindio.edu.edusoft.model.entities.CourseEvent;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CourseEventMapper {

    CourseEventMapper INSTANCE = Mappers.getMapper(CourseEventMapper.class);

    @Mapping(source = "eventType", target = "eventType")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "createdAt", target = "createdAt")
    @Mapping(source = "user.email", target = "createdBy")
    @Mapping(source = "course.id", target = "courseId")
    @Mapping(source = "module.id", target = "moduleId")
    @Mapping(source = "lesson.id", target = "lessonId")
    @Mapping(source = "content.id", target = "contentId")
    CourseEventResponseDto toResponseDto(CourseEvent event);

    List<CourseEventResponseDto> toResponseDtoList(List<CourseEvent> events);
}


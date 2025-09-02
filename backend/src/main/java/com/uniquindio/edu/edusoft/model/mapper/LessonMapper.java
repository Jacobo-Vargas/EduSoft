package com.uniquindio.edu.edusoft.model.mapper;

import com.uniquindio.edu.edusoft.model.dto.lesson.LessonRequestDto;
import com.uniquindio.edu.edusoft.model.dto.lesson.LessonResponseDto;
import com.uniquindio.edu.edusoft.model.entities.Lesson;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring")
public interface LessonMapper {

    @Mapping(source = "moduleId", target = "module.id")
    Lesson toEntity(LessonRequestDto dto);

    @Mapping(source = "module.id", target = "moduleId")
    @Mapping(source = "module.name", target = "moduleName")
    @Mapping(source = "module.course.id", target = "courseId")
    @Mapping(source = "module.course.title", target = "courseName")
    @Mapping(target = "contentsCount", expression = "java(entity.getContents() != null ? entity.getContents().size() : 0)")
    LessonResponseDto toResponseDto(Lesson entity);

    List<LessonResponseDto> toResponseDtoList(List<Lesson> entities);
}
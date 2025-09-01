package com.uniquindio.edu.edusoft.model.mapper;

import com.uniquindio.edu.edusoft.model.dto.module.ModuleRequestDto;
import com.uniquindio.edu.edusoft.model.dto.module.ModuleResponseDto;
import com.uniquindio.edu.edusoft.model.entities.Module;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ModuleMapper {

    @Mapping(source = "courseId", target = "course.id")
    Module toEntity(ModuleRequestDto dto);

    @Mapping(source = "course.id", target = "courseId")
    @Mapping(source = "course.title", target = "courseName")
    @Mapping(target = "lessonsCount", expression = "java(entity.getLessons() != null ? entity.getLessons().size() : 0)")
    ModuleResponseDto toResponseDto(Module entity);

    List<ModuleResponseDto> toResponseDtoList(List<Module> entities);
}
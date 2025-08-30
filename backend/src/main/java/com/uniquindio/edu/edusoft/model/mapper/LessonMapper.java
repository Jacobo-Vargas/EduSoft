package com.uniquindio.edu.edusoft.model.mapper;

import com.uniquindio.edu.edusoft.model.dto.lesson.RequestLessonDTO;
import com.uniquindio.edu.edusoft.model.dto.lesson.ResponseLessonDTO;
import com.uniquindio.edu.edusoft.model.entities.Lesson;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LessonMapper {

    Lesson toEntity(RequestLessonDTO requestLessonDTO);

    @Mapping(source = "module.id", target = "moduleId")
    @Mapping(source = "module.title", target = "moduleTitle")
    ResponseLessonDTO toResponseDTO(Lesson lesson);

    List<ResponseLessonDTO> toResponseDTOList(List<Lesson> lessons);
}

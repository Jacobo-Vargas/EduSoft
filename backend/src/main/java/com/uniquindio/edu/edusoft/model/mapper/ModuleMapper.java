package com.uniquindio.edu.edusoft.model.mapper;

import com.uniquindio.edu.edusoft.model.dto.module.RequestModuleDTO;
import com.uniquindio.edu.edusoft.model.dto.module.ResponseModuleDTO;
import com.uniquindio.edu.edusoft.model.entities.Module;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ModuleMapper {

    Module toEntity(RequestModuleDTO requestModuleDTO);

    @Mapping(source = "course.id", target = "courseId")
    @Mapping(source = "course.title", target = "courseTitle")
    ResponseModuleDTO toResponseDTO(Module module);

    List<ResponseModuleDTO> toResponseDTOList(List<Module> modules);
}

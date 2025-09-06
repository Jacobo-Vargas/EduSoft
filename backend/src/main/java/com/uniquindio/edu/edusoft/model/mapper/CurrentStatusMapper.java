package com.uniquindio.edu.edusoft.model.mapper;


import com.uniquindio.edu.edusoft.model.dto.CurrentStatus.CurrentStatusRequestDto;
import com.uniquindio.edu.edusoft.model.dto.CurrentStatus.CurrentStatusResponseDto;
import com.uniquindio.edu.edusoft.model.entities.CurrentStatus;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
    public interface CurrentStatusMapper {

        CurrentStatusMapper INSTANCE = Mappers.getMapper(CurrentStatusMapper.class);

        // Entity → Response DTO
        CurrentStatusResponseDto toResponseDto(CurrentStatus currentStatus);

        List<CurrentStatusResponseDto> toResponseDtoList(List<CurrentStatus> currentStatuses);

       
        @Mapping(target = "id", ignore = true) // ignore id for new records
        CurrentStatus toEntity(CurrentStatusRequestDto dto);
    }


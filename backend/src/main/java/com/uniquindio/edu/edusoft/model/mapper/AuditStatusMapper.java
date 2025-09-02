package com.uniquindio.edu.edusoft.model.mapper;

import com.uniquindio.edu.edusoft.model.dto.auditStatus.AuditStatusRequestDto;
import com.uniquindio.edu.edusoft.model.dto.auditStatus.AuditStatusResponseDto;
import com.uniquindio.edu.edusoft.model.dto.category.CategoryRequestDto;
import com.uniquindio.edu.edusoft.model.dto.category.CategoryResponseDto;
import com.uniquindio.edu.edusoft.model.entities.AuditStatus;
import com.uniquindio.edu.edusoft.model.entities.Category;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AuditStatusMapper {

    AuditStatusMapper INSTANCE = Mappers.getMapper(AuditStatusMapper.class);

    AuditStatus toEntity(AuditStatusRequestDto dto);

    AuditStatusResponseDto toResponseDto(AuditStatus auditStatus);

    List<AuditStatusResponseDto> toResponseDtoList(List<AuditStatus> auditStatuses);
}
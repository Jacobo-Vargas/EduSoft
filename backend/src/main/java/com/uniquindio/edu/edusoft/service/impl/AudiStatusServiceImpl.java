package com.uniquindio.edu.edusoft.service.impl;

import com.uniquindio.edu.edusoft.model.dto.auditStatus.AuditStatusRequestDto;
import com.uniquindio.edu.edusoft.model.dto.category.CategoryRequestDto;
import com.uniquindio.edu.edusoft.model.dto.category.CategoryResponseDto;
import com.uniquindio.edu.edusoft.model.entities.AuditStatus;
import com.uniquindio.edu.edusoft.model.entities.Category;
import com.uniquindio.edu.edusoft.model.mapper.AuditStatusMapper;
import com.uniquindio.edu.edusoft.repository.AuditStatusRepository;
import com.uniquindio.edu.edusoft.service.AudiStatusService;
import com.uniquindio.edu.edusoft.utils.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AudiStatusServiceImpl implements AudiStatusService {
    private final AuditStatusRepository auditStatusRepository;
    private  final AuditStatusMapper auditStatusMapper;

    @Override
    public ResponseEntity<?> createStatusAudi(AuditStatusRequestDto auditStatusRequestDto) {
        AuditStatus auditStatus = auditStatusMapper.toEntity(auditStatusRequestDto);
        if(auditStatusRepository.existsByNameIgnoreCase(auditStatusRequestDto.getName())){
            ResponseData<String> response = new ResponseData<>(null, "El estado ingresado ya fue registrado", "success");
            return ResponseEntity.badRequest().body(response);
        }
        AuditStatus saved = auditStatusRepository.save(auditStatus);
        return  ResponseEntity.ok().body(auditStatus);
    }
}

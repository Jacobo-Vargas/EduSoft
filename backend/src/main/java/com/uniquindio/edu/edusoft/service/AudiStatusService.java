package com.uniquindio.edu.edusoft.service;

import com.uniquindio.edu.edusoft.model.dto.auditStatus.AuditStatusRequestDto;
import com.uniquindio.edu.edusoft.model.entities.AuditStatus;
import org.springframework.http.ResponseEntity;

public interface AudiStatusService {
    public ResponseEntity<?> createStatusAudi(AuditStatusRequestDto auditStatusRequestDto);
}

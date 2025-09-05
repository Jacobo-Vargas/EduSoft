package com.uniquindio.edu.edusoft.controller;

import com.uniquindio.edu.edusoft.model.dto.auditStatus.AuditStatusRequestDto;
import com.uniquindio.edu.edusoft.service.AudiStatusService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/audiStatus")
@RequiredArgsConstructor
public class AuditStatusController {

    private final AudiStatusService audiStatusService;

    @PostMapping("/createAudiStatus")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> createStatusAudi(@RequestBody AuditStatusRequestDto auditStatusRequestDto, HttpServletResponse response) throws Exception {
        return this.audiStatusService.createStatusAudi(auditStatusRequestDto);
    }

}

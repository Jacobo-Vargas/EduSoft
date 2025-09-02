package com.uniquindio.edu.edusoft.controller;

import com.uniquindio.edu.edusoft.model.dto.auditStatus.AuditStatusRequestDto;
import com.uniquindio.edu.edusoft.model.dto.respose.LoginRequestDTO;
import com.uniquindio.edu.edusoft.service.AudiStatusService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/audiStatus")
@RequiredArgsConstructor
public class AudiStatusController {

    private final AudiStatusService audiStatusService;
    @PostMapping("/createAudiStatus")
    public ResponseEntity<?> createStatusAudi(@RequestBody AuditStatusRequestDto auditStatusRequestDto, HttpServletResponse response) throws Exception {
        return this.audiStatusService.createStatusAudi(auditStatusRequestDto);
    }

}

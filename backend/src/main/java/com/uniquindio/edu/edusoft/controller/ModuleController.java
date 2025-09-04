package com.uniquindio.edu.edusoft.controller;

import com.uniquindio.edu.edusoft.model.dto.module.ModuleRequestDto;
import com.uniquindio.edu.edusoft.model.dto.module.ModuleResponseDto;
import com.uniquindio.edu.edusoft.model.dto.common.ReorderRequestDto;
import com.uniquindio.edu.edusoft.service.ModuleService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/modules")
@RequiredArgsConstructor
public class ModuleController {

    private final ModuleService moduleService;

    @PostMapping
    public ResponseEntity<ModuleResponseDto> createModule(
            @RequestBody @Valid ModuleRequestDto moduleRequestDto,
            Authentication authentication) throws Exception {
        String userEmail = authentication.getName();
        return moduleService.createModule(moduleRequestDto, userEmail);
    }

    @PutMapping("/{moduleId}")
    public ResponseEntity<ModuleResponseDto> updateModule(
            @PathVariable Long moduleId,
            @RequestBody @Valid ModuleRequestDto moduleRequestDto,
            Authentication authentication) throws Exception {
        String userId = authentication.getName();
        return moduleService.updateModule(moduleId, moduleRequestDto, userId);
    }

    @DeleteMapping("/{moduleId}")
    public ResponseEntity<?> deleteModule(
            @PathVariable Long moduleId,
            Authentication authentication) throws Exception {
        String userId = authentication.getName();
        return moduleService.deleteModule(moduleId, userId);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<ModuleResponseDto>> getModulesByCourse(
            @PathVariable Long courseId) throws Exception {
        return moduleService.getModulesByCourse(courseId);
    }

    @GetMapping("/{moduleId}")
    public ResponseEntity<ModuleResponseDto> getModuleById(
            @PathVariable Long moduleId) throws Exception {
        return moduleService.getModuleById(moduleId);
    }

    @PutMapping("/course/{courseId}/reorder")
    public ResponseEntity<?> reorderModules(
            @PathVariable Long courseId,
            @RequestBody @Valid ReorderRequestDto reorderRequest,
            Authentication authentication) throws Exception {
        String userId = authentication.getName();
        return moduleService.reorderModules(courseId, reorderRequest, userId);
    }

    @PutMapping("/{moduleId}/toggle-visibility")
    public ResponseEntity<?> toggleVisibility(
            @PathVariable Long moduleId,
            Authentication authentication) throws Exception {
        String userId = authentication.getName();
        return moduleService.toggleModuleVisibility(moduleId, userId);
    }

    @PutMapping("/{moduleId}/status")
    public ResponseEntity<?> changeStatus(
            @PathVariable Long moduleId,
            @RequestParam String status,
            Authentication authentication) throws Exception {
        String userId = authentication.getName();
        return moduleService.changeModuleStatus(moduleId, status, userId);
    }
}
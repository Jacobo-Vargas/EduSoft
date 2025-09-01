package com.uniquindio.edu.edusoft.service;

import com.uniquindio.edu.edusoft.model.dto.module.ModuleRequestDto;
import com.uniquindio.edu.edusoft.model.dto.module.ModuleResponseDto;
import com.uniquindio.edu.edusoft.model.dto.common.ReorderRequestDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ModuleService {

    ResponseEntity<ModuleResponseDto> createModule(ModuleRequestDto moduleRequestDto, String userId) throws Exception;

    ResponseEntity<ModuleResponseDto> updateModule(Long moduleId, ModuleRequestDto moduleRequestDto, String userId) throws Exception;

    ResponseEntity<?> deleteModule(Long moduleId, String userId) throws Exception;

    ResponseEntity<List<ModuleResponseDto>> getModulesByCourse(Long courseId) throws Exception;

    ResponseEntity<ModuleResponseDto> getModuleById(Long moduleId) throws Exception;

    ResponseEntity<?> reorderModules(Long courseId, ReorderRequestDto reorderRequest, String userId) throws Exception;

    ResponseEntity<?> toggleModuleVisibility(Long moduleId, String userId) throws Exception;

    ResponseEntity<?> changeModuleStatus(Long moduleId, String status, String userId) throws Exception;
}
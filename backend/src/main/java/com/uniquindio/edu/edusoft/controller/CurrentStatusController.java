package com.uniquindio.edu.edusoft.controller;

import com.uniquindio.edu.edusoft.model.dto.CurrentStatus.CurrentStatusRequestDto;
import com.uniquindio.edu.edusoft.model.dto.CurrentStatus.CurrentStatusResponseDto;
import com.uniquindio.edu.edusoft.service.CurrentStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/currentStatuses")
@RequiredArgsConstructor
public class CurrentStatusController {
    private final CurrentStatusService currentStatusService;

    // Crear un nuevo estado
    @PostMapping
    public ResponseEntity<CurrentStatusResponseDto> createCurrentStatus(
            @Valid @RequestBody CurrentStatusRequestDto dto) {
        return ResponseEntity.ok(currentStatusService.createCurrentStatus(dto));
    }

    // Obtener todos los estados
    @GetMapping
    public ResponseEntity<List<CurrentStatusResponseDto>> getAllCurrentStatuses() {
        return ResponseEntity.ok(currentStatusService.getAllCurrentStatuses());
    }

    // Obtener estado por ID
    @GetMapping("/{id}")
    public ResponseEntity<CurrentStatusResponseDto> getCurrentStatusById(@PathVariable Long id) {
        return ResponseEntity.ok(currentStatusService.getCurrentStatusById(id));
    }

    // Actualizar un estado
    @PutMapping("/{id}")
    public ResponseEntity<CurrentStatusResponseDto> updateCurrentStatus(
            @PathVariable Long id, @Valid @RequestBody CurrentStatusRequestDto dto) {
        return ResponseEntity.ok(currentStatusService.updateCurrentStatus(id, dto));
    }

    // Eliminar un estado
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCurrentStatus(@PathVariable Long id) {
        currentStatusService.deleteCurrentStatus(id);
        return ResponseEntity.noContent().build();
    }
}

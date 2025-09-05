package com.uniquindio.edu.edusoft.controller;

import com.uniquindio.edu.edusoft.model.dto.CurrentStatus.CurrentStatusRequestDto;
import com.uniquindio.edu.edusoft.model.dto.CurrentStatus.CurrentStatusResponseDto;
import com.uniquindio.edu.edusoft.model.dto.category.CategoryResponseDto;
import com.uniquindio.edu.edusoft.service.CurrentStatusService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/currentStatuses")
@RequiredArgsConstructor
public class CurrentStatusController {
    private final CurrentStatusService currentStatusService;

    // Crear un nuevo estado
    @PostMapping
    public ResponseEntity<?> createCurrentStatus(
            @Valid @RequestBody CurrentStatusRequestDto dto) {
       CurrentStatusResponseDto result = currentStatusService.createCurrentStatus(dto);
        if (result == null) {
            // 400 con un JSON { "message": "..." }
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("message", "Ya existe una categoría con ese nombre"));
        }
        return ResponseEntity.ok(result);
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

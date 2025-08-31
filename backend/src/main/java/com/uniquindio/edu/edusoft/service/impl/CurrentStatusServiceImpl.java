package com.uniquindio.edu.edusoft.service.impl;

import com.uniquindio.edu.edusoft.model.dto.CurrentStatus.CurrentStatusRequestDto;
import com.uniquindio.edu.edusoft.model.dto.CurrentStatus.CurrentStatusResponseDto;
import com.uniquindio.edu.edusoft.model.entities.CurrentStatus;
import com.uniquindio.edu.edusoft.model.mapper.CurrentStatusMapper;
import com.uniquindio.edu.edusoft.repository.CurrentStatusRepository;
import com.uniquindio.edu.edusoft.service.CurrentStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CurrentStatusServiceImpl implements CurrentStatusService {

    private final CurrentStatusRepository currentStatusRepository;
    private final CurrentStatusMapper currentStatusMapper;
    // Crear un nuevo estado
    public CurrentStatusResponseDto createCurrentStatus(CurrentStatusRequestDto dto) {
        CurrentStatus currentStatus = currentStatusMapper.toEntity(dto);
        CurrentStatus saved = currentStatusRepository.save(currentStatus);
        return currentStatusMapper.toResponseDto(saved);
    }
    // Obtener todos los estados
    public List<CurrentStatusResponseDto> getAllCurrentStatuses() {
        return currentStatusMapper.toResponseDtoList(currentStatusRepository.findAll());
    }
    // Obtener estado por ID
    public CurrentStatusResponseDto getCurrentStatusById(Long id) {
        CurrentStatus currentStatus = currentStatusRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estado no encontrado"));
        return currentStatusMapper.toResponseDto(currentStatus);
    }
    // Actualizar un estado (opcional)
    public CurrentStatusResponseDto updateCurrentStatus(Long id, CurrentStatusRequestDto dto) {
        CurrentStatus currentStatus = currentStatusRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estado no encontrado"));
        currentStatus.setName(dto.getName());
        currentStatus.setDescription(dto.getDescription());
        CurrentStatus updated = currentStatusRepository.save(currentStatus);
        return currentStatusMapper.toResponseDto(updated);
    }
    // Eliminar un estado
    public void deleteCurrentStatus(Long id) {
        currentStatusRepository.deleteById(id);
    }
}

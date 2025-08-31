package com.uniquindio.edu.edusoft.service;

import com.uniquindio.edu.edusoft.model.dto.CurrentStatus.CurrentStatusRequestDto;
import com.uniquindio.edu.edusoft.model.dto.CurrentStatus.CurrentStatusResponseDto;

import java.util.List;

public interface CurrentStatusService {
    public CurrentStatusResponseDto createCurrentStatus(CurrentStatusRequestDto dto);
    public List<CurrentStatusResponseDto> getAllCurrentStatuses();
    public CurrentStatusResponseDto getCurrentStatusById(Long id);
    public CurrentStatusResponseDto updateCurrentStatus(Long id, CurrentStatusRequestDto dto);
    public void deleteCurrentStatus(Long id);
}

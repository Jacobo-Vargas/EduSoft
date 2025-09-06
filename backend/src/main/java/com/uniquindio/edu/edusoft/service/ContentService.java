package com.uniquindio.edu.edusoft.service;

import com.uniquindio.edu.edusoft.model.dto.content.ContentRequestDto;
import com.uniquindio.edu.edusoft.model.dto.content.ContentResponseDto;
import com.uniquindio.edu.edusoft.model.dto.content.ContentAssignmentDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface ContentService {

    ResponseEntity<ContentResponseDto> createContent(ContentRequestDto dto, String userEmail) throws Exception;

    ResponseEntity<List<ContentResponseDto>> getContentsByCourse(Long courseId) throws Exception;

    ResponseEntity<List<ContentResponseDto>> getContentsByLesson(Long lessonId) throws Exception;

    ResponseEntity<?> assignContentsToLesson(ContentAssignmentDto dto, String userEmail) throws Exception;

    ResponseEntity<?> unassignContent(Long contentId, String userEmail) throws Exception;

    ResponseEntity<?> deleteContent(Long contentId, String userEmail) throws Exception;
}

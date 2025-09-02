package com.uniquindio.edu.edusoft.service;

import com.uniquindio.edu.edusoft.model.dto.lesson.LessonRequestDto;
import com.uniquindio.edu.edusoft.model.dto.lesson.LessonResponseDto;
import com.uniquindio.edu.edusoft.model.dto.common.ReorderRequestDto;
import com.uniquindio.edu.edusoft.model.dto.content.ContentAssignmentDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface LessonService {

    ResponseEntity<LessonResponseDto> createLesson(LessonRequestDto lessonRequestDto, String userId) throws Exception;

    ResponseEntity<LessonResponseDto> updateLesson(Long lessonId, LessonRequestDto lessonRequestDto, String userId) throws Exception;

    ResponseEntity<?> deleteLesson(Long lessonId, String userId) throws Exception;

    ResponseEntity<List<LessonResponseDto>> getLessonsByModule(Long moduleId) throws Exception;

    ResponseEntity<LessonResponseDto> getLessonById(Long lessonId) throws Exception;

    ResponseEntity<?> reorderLessons(Long moduleId, ReorderRequestDto reorderRequest, String userId) throws Exception;

    ResponseEntity<?> moveLessonToModule(Long lessonId, Long newModuleId, String userId) throws Exception;

    ResponseEntity<?> toggleLessonVisibility(Long lessonId, String userId) throws Exception;

    ResponseEntity<?> changeLessonStatus(Long lessonId, String status, String userId) throws Exception;

    ResponseEntity<?> assignContentsToLesson(ContentAssignmentDto assignmentDto, String userId) throws Exception;

    ResponseEntity<?> unassignContentFromLesson(Long contentId, String userId) throws Exception;

    ResponseEntity<?> reorderContentsInLesson(Long lessonId, ReorderRequestDto reorderRequest, String userId) throws Exception;
}